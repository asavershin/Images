package com.github.asavershin.api.config;

import com.github.asavershin.api.config.properties.MinIOProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for MinIO client.
 *
 * @author Asavershin
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MinIOConfig {

    /**
     * The MinIO properties.
     */
    private final MinIOProperties minioProperties;

    /**
     * Creates a MinioClient instance.
     *
     * @return A MinioClient instance configured with the provided
     * MinIO properties.
     */
    @Bean
    public MinioClient minioClient() {
        log.info(minioProperties.toString());
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getUser(),
                        minioProperties.getPassword())
                .build();
    }
}
