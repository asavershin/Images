package com.github.asavershin.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Secret key used for signing JWT tokens.
     */
    private String secret;

    /**
     * Expiration time for access tokens in milliseconds.
     */
    private Long accessExpiration;

    /**
     * Expiration time for refresh tokens in milliseconds.
     */
    private Long refreshExpiration;

    /**
     * Constants for the start of the token in the Authorization header.
     */
    public static final int START_OF_TOKEN = 7;
}
