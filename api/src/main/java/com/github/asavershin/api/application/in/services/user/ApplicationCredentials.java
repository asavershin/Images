package com.github.asavershin.api.application.in.services.user;

import lombok.Getter;

@Getter
public class ApplicationCredentials {
    /**
     * The access token for user.
     */
    private final String accessToken;

    /**
     * The refresh token for user.
     */
    private final String refreshToken;

    /**
     * Constructs an instance of {@link ApplicationCredentials}
     * with the provided access token and refresh token.
     *
     * @param aAccessToken The access token for user.
     * @param aRefreshToken The refresh token for user.
     */
    public ApplicationCredentials(final String aAccessToken,
                                  final String aRefreshToken) {
        this.accessToken = aAccessToken;
        this.refreshToken = aRefreshToken;
    }
}
