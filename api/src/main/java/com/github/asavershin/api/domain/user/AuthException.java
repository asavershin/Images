package com.github.asavershin.api.domain.user;

/**
 * Represents an exception that occurs during authentication process.
 *
 * @author asavershin
 */
public class AuthException extends RuntimeException {

    /**
     * Constructs an instance of AuthException with
     * the specified detail message.
     *
     * @param message the detail message.
     *                The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AuthException(final String message) {
        super(message);
    }
}
