package com.github.asavershin.api.domain;

/**
 * Represents an exception that is thrown
 * when there is an issue with resource ownership.
 * For example, a user requests someone else's picture
 * @author Asavershin
 */
public class ResourceOwnershipException extends RuntimeException {

    /**
     * Constructs a new instance of the ResourceOwnershipException
     * with the specified error message.
     *
     * @param message the error message
     */
    public ResourceOwnershipException(final String message) {
        super(message);
    }
}
