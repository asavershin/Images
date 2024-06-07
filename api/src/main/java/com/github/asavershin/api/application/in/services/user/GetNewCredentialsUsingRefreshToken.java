package com.github.asavershin.api.application.in.services.user;

import com.github.asavershin.api.domain.user.Credentials;

@FunctionalInterface
public interface GetNewCredentialsUsingRefreshToken {
    /**
     * Generates new credentials using the provided refresh token.
     *
     * @param credentials The credentials that contain the refresh token.
     * @return The new credentials generated using the refresh token.
     */
    ApplicationCredentials get(Credentials credentials);
}
