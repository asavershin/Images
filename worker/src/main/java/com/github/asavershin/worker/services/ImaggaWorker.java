package com.github.asavershin.worker.services;

import com.github.asavershin.worker.BadImaggaCodeException;
import com.github.asavershin.worker.config.ImaggaProperties;
import com.github.asavershin.worker.config.MinIOProperties;
import com.github.asavershin.worker.dto.ImaggaTagsResponse;
import com.github.asavershin.worker.dto.ImaggaUploadResponse;
import com.github.asavershin.worker.out.MinioService;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("imagga")
@RequiredArgsConstructor
@Slf4j
public class ImaggaWorker implements Worker {
    private final MinioService imageStorage;
    private final MinIOProperties minioProps;
    private final ImaggaProperties imaggaProps;
    private final RestTemplate restTemplate;
    private static String ENDPOINT_UPLOAD = "https://api.imagga.com/v2/uploads";
    private static final String ENDPOINT_TAGS = "https://api.imagga.com/v2/tags";
    private final Bucket bucket;


    @Override
    @Retryable(
            retryFor = {BadImaggaCodeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public String doWork(final String imageUUID, final boolean lastWorker) {
        if (!bucket.tryConsume(1)) {
            throw new RuntimeException("Rate limit exceeded");
        }
        log.info("Start Work");
        InputStream is = imageStorage.getFile(imageUUID);
        log.info("End get file");
        var uploadId = requestToImagga(is);
        ImaggaTagsResponse tagsResponse = getTags(uploadId.orElseThrow()).orElseThrow();
        log.info("Start sending to minio");
        // Add tags to image
        byte[] modifiedImage = addTagsToImage(imageStorage.getFile(imageUUID), tagsResponse.getResult().getTags());

        // Generate new UUID for the modified image
        String id = UUID.randomUUID().toString();
        if (!lastWorker) {
            id = minioProps.getTtlprefix() + id;
        }
        // Save the modified image to MinIO
        imageStorage.saveFile(modifiedImage, id);
        log.info("End imagga worker");
        return id;
    }
    @Recover
    public String recoverAfterRetryLimitReached(
            final BadImaggaCodeException ex,
            final String imageUUID,
            final boolean lastWorker
    ){
        log.error("Imagga api is not responding");
        return imageUUID;
    }

    @Override
    public String whoAmI() {
        return "ROTATE";
    }

    private Optional<String> requestToImagga(final InputStream is) {
        log.info("start send request");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("Add body");
        ByteArrayResource byteArrayResource;
        try {
            byteArrayResource = new ByteArrayResource(toByteArray(is)) {
                @Override
                public String getFilename() {
                    return "upload.jpg"; // You can set the filename if needed
                }
            };
        } catch (IOException e) {
            log.error("Error reading InputStream", e);
            return Optional.empty();
        }

        body.add("image", byteArrayResource);
        log.info("Add headers");


        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(imaggaProps.getKey(), imaggaProps.getSecret());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        log.info("create req entity");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        log.info("Send to imagga");
        ResponseEntity<ImaggaUploadResponse> response = restTemplate.exchange(ENDPOINT_UPLOAD, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("end send request");
        if (!checkResponseCode(response)) {
            return Optional.empty();
        }
        return Optional.of(Objects.requireNonNull(response.getBody()).getResult().getUploadId());
    }

    private byte[] toByteArray(final InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    private Optional<ImaggaTagsResponse> getTags(final String uploadId) {
        log.info("Start getTags");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(imaggaProps.getKey(), imaggaProps.getSecret());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var builder = UriComponentsBuilder.fromHttpUrl(ENDPOINT_TAGS)
                .queryParam("image_upload_id", uploadId)
                .queryParam("limit", 3);
        var url = builder.build().encode().toUriString();

        log.info("Sending GET request to: {}", url);
        ResponseEntity<ImaggaTagsResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });

        if (checkResponseCode(response)) {
            log.info("Get tags successful");
            return Optional.ofNullable(response.getBody());
        } else {
            log.error("Get tags failed with status code: " + response.getStatusCode());
            return Optional.empty();
        }
    }

    private byte[] addTagsToImage(
            final InputStream is,
            final List<ImaggaTagsResponse.Tag> tags
    ) {
        // Load image with ImageJ
        try {
            var image = ImageIO.read(is);
            var font = new Font("Arial", Font.BOLD, 50);
            // Add tags to the image
            int y = 0; // Starting Y position for text
            Graphics g = image.getGraphics();
            g.setFont(font);
            g.setColor(Color.GREEN);
            for (ImaggaTagsResponse.Tag tag : tags) {
                g.drawString(tag.getTag().getEn(), 250, y);
                y += 50; // Move to next line
            }
            // Convert ImagePlus to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            g.dispose();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean checkResponseCode(final ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Upload successful");
            return true;
        } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS
                || response.getStatusCode().is5xxServerError()) {
            log.error("Upload failed with status code: " + response.getStatusCode());
            throw new BadImaggaCodeException("Upload failed with status code: " + response.getStatusCode());
        } else {
            log.error("Upload failed with status code: " + response.getStatusCode());
            return false;
        }
    }
}
