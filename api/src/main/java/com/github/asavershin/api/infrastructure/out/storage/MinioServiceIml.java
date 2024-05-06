package com.github.asavershin.api.infrastructure.out.storage;


import com.github.asavershin.api.application.out.MinioService;
import com.github.asavershin.api.config.properties.MinIOProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketLifecycleArgs;
import io.minio.messages.Expiration;
import io.minio.messages.LifecycleConfiguration;
import io.minio.messages.LifecycleRule;
import io.minio.messages.RuleFilter;
import io.minio.messages.Status;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class MinioServiceIml implements MinioService {
    /**
     * The MinioClient is used to interact with the MinIO server.
     */
    private final MinioClient minioClient;
    /**
     * The MinIO properties from .yml.
     */
    private final MinIOProperties minioProperties;

    /**
     * Constructor for {@link MinioServiceIml}.
     *
     * @param aMinioClient     The {@link MinioClient}
     *                         instance to interact with the MinIO server.
     * @param aMinioProperties The {@link MinIOProperties}
     *                         instance containing the configuration
     *                         for the MinIO server.
     */
    public MinioServiceIml(final MinioClient aMinioClient,
                           final MinIOProperties aMinioProperties) {
        this.minioClient = aMinioClient;
        this.minioProperties = aMinioProperties;
        createBucket();
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void saveFile(final MultipartFile image, final String filename) {

        if (!bucketExists(minioProperties.getBucket())) {
            throw new FileException(
                    "File upload failed: bucket does not exist"
            );
        }

        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new FileException("File must have name");
        }
        InputStream inputStream;
        try {
            inputStream = image.getInputStream();
        } catch (Exception e) {
            throw new FileException("File upload failed: "
                    + e.getMessage());
        }
        saveFile(inputStream, filename);
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public byte[] getFile(final String link) {
        if (link == null) {
            throw new FileException("File download failed: link is nullable");
        }
        try {
            return IOUtils.toByteArray(
                    minioClient.getObject(GetObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(link)
                            .build()));
        } catch (Exception e) {
            throw new FileException("File download failed: " + e.getMessage());
        }
    }

    /**
     * Not final to allow spring use proxy.
     */
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
        boolean found = bucketExists(minioProperties.getBucket());
        if (!found) {
            List<LifecycleRule> rules = new LinkedList<>();
            rules.add(
                    new LifecycleRule(
                            Status.ENABLED,
                            null,
                            new Expiration((ZonedDateTime) null,
                                    Integer.valueOf(
                                            minioProperties.getExpiration()
                                    ),
                                    null),
                            new RuleFilter(minioProperties.getTtlprefix()),
                            "rule1",
                            null,
                            null,
                            null));
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
            minioClient.setBucketLifecycle(
                    SetBucketLifecycleArgs.builder().bucket(
                                    minioProperties.getBucket()
                            )
                            .config(
                                    new LifecycleConfiguration(rules)
                            ).build());
        }

    }

    @SneakyThrows
    private void saveFile(
            final InputStream inputStream,
            final String fileName
    ) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

    @SneakyThrows(Exception.class)
    private boolean bucketExists(final String bucketName) {
        return minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );
    }
}
