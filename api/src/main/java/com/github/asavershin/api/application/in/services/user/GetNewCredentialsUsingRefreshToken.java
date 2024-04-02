package com.github.asavershin.api.application.in.services.user;

import com.github.asavershin.api.domain.user.Credentials;

@FunctionalInterface
public interface GetNewCredentialsUsingRefreshToken {
    ApplicationCredentials get(Credentials credentials);
}
