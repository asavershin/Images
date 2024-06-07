package com.github.asavershin.api.domain.user;

/**
 * A functional interface that represents a method to register a new user.
 *
 */
@FunctionalInterface
public interface RegisterNewUser {
    /**
     * Registers a new user with the provided full name and credentials.
     *
     * @param fullName The FullName value object
     *                 containing the user's full name.
     * @param credentials The Credentials value object
     *                   containing the user's credentials.
     */
    void register(FullName fullName, Credentials credentials);
}
