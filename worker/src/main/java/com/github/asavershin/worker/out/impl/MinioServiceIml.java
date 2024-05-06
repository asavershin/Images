package com.github.asavershin.worker.out.impl;


import com.github.asavershin.worker.config.MinIOProperties;
import com.github.asavershin.worker.out.FileException;
import com.github.asavershin.worker.out.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketLifecycleArgs;
import io.minio.messages.Expiration;
import io.minio.messages.LifecycleConfiguration;
import io.minio.messages.LifecycleRule;
import io.minio.messages.RuleFilter;
import io.minio.messages.Status;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
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
        createBuckets();
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void saveFile(byte[] image, final String filename) {

        if (!bucketExists(minioProperties.getBucket())) {
            throw new FileException(
                    "File upload failed: bucket does not exist"
            );
        }

        if (image.length == 0) {
            throw new FileException("File array could not be empty");
        }
        try (var bais = new ByteArrayInputStream(image)) {
            saveFile(bais, filename);
        } catch (Exception e) {
            throw new FileException("File upload failed: "
                    + e.getMessage());
        }
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public InputStream getFile(final String link) {
        if (link == null) {
            throw new FileException("File download failed: link is nullable");
        }
        try {
            InputStream obj = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(link)
                    .build());
            return obj;
        } catch (Exception e) {
            throw new FileException("File " + link + " download failed: " + e.getMessage());
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

    @SneakyThrows
    private void createBuckets() {
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

    @SneakyThrows(Exception.class)
    private boolean bucketExists(final String bucketName) {
        return minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );
    }
}
