package com.github.asavershin.api.domain.user;

/**
 * A functional interface representing a login operation for user
 * using login, password.
 *
 * @author asavershin
 * @since 1.0
 */
@FunctionalInterface
public interface TryToLogin {
    /**
     * Attempts to log the user in using the provided credentials.
     *
     * @param credentials The credentials to use for login.
     * @return An instance of {@link AuthenticatedUser}
     * if the login is successful, otherwise null.
     */
    AuthenticatedUser login(Credentials credentials);
}
