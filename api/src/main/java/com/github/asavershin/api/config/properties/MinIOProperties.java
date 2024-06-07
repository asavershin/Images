package com.github.asavershin.api.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "minio")
@NoArgsConstructor
public class MinIOProperties {
    /**
     * The name of the bucket in the MinIO service.
     */
    private String bucket;

    /**
     * The URL of the MinIO service.
     */
    private String url;

    /**
     * The username for the MinIO service.
     */
    private String user;

    /**
     * The password for the MinIO service.
     */
    private String password;
}
