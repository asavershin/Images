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
     * @param newUser The new user to register
     */
    void register(User newUser);
}
