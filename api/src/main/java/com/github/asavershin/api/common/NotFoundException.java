package com.github.asavershin.api.common;

public class NotFoundException extends RuntimeException {
    /**
     * @param message specifies information about the object not found
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
