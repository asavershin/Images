package com.github.asavershin.api.config;

import com.github.asavershin.api.config.properties.MinIOProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MinIOConfig {
    private final MinIOProperties minioProperties;
    @Bean
    public MinioClient minioClient() {
        log.info("MinIOConfigLog");
        log.info(minioProperties.toString());
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getUser(),
                        minioProperties.getPassword())
                .build();
    }
}
