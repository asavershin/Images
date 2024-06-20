package com.github.asavershin.api.common;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    /**
     * Constructs a new instance of {@code NotFoundException}
     * with the specified error message.
     *
     * @param message the error message
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
