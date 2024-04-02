package com.github.asavershin.api.infrastructure.out.storage;


import com.github.asavershin.api.application.out.MinioService;
import com.github.asavershin.api.config.properties.MinIOProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class MinioServiceIml implements MinioService {
    private final MinioClient minioClient;
    private final MinIOProperties minioProperties;

    public MinioServiceIml(MinioClient minioClient, MinIOProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        createBucket();
    }

    @Override
    public String saveFile(final MultipartFile image) {

        if (!bucketExists(minioProperties.getBucket())) {
            throw new FileException("File upload failed: bucket does not exist");
        }

        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new FileException("File must have name");
        }
        var link = generateFileName();
        InputStream inputStream;
        try {
            inputStream = image.getInputStream();
        } catch (Exception e) {
            throw new FileException("File upload failed: "
                    + e.getMessage());
        }
        saveImage(inputStream, link);
        return link;
    }

    @Override
    public byte[] getFile(final String link) {
        if (link == null) {
            throw new FileException("File download failed: link is nullable");
        }
        try {
            return IOUtils.toByteArray(minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(link)
                    .build()));
        } catch (Exception e) {
            throw new FileException("File download failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteFiles(final List<String> links) {
        if (links == null || links.isEmpty()) {
            return;
        }
        if (!bucketExists(minioProperties.getBucket())) {
            throw new FileException("Minio bucket doesn't exist");
        }
        try {
            for (var link : links) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(link)
                                .build());
            }
        } catch (Exception e) {
            throw new FileException("Failed to delete file: " + e.getMessage());
        }
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    @SneakyThrows
    private void saveImage(
            final InputStream inputStream,
            final String fileName
    ) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

    private String generateFileName() {
        return UUID.randomUUID().toString();
    }

    @SneakyThrows(Exception.class)
    private boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }
}
