package com.github.asavershin.api.application.in.services.user;

import com.github.asavershin.api.domain.user.Credentials;

@FunctionalInterface
public interface GetNewCredentials {
    /**
     * Generates new credentials for the user based on the provided credentials.
     *
     * @param credentials The current credentials of the user.
     * @return The new credentials for the user.
     */
    ApplicationCredentials get(Credentials credentials);
}
