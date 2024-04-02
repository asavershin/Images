package com.github.asavershin.api.domain.user;

@FunctionalInterface
public interface TryToLogin {
    AuthenticatedUser login(Credentials credentials);
}
