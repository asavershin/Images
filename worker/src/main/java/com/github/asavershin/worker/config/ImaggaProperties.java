package com.github.asavershin.worker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "imagga")
@Getter
@Setter
public class ImaggaProperties {
    private String key;
    private String secret;
    private String ttlprefix;
}
