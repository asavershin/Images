package com.github.asavershin.api.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "minio")
@NoArgsConstructor
public class MinIOProperties {
    private String bucket;
    private String url;
    private String user;
    private String password;
}
