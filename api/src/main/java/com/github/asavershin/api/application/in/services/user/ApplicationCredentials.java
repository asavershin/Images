package com.github.asavershin.api.application.in.services.user;

import lombok.Getter;

@Getter
public class ApplicationCredentials {
    private final String accessToken;
    private final String refreshToken;

    public ApplicationCredentials(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
