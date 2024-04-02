package com.github.asavershin.api.domain.user;

@FunctionalInterface
public interface RegisterNewUser {
    void register(FullName fullName, Credentials credentials);
}
