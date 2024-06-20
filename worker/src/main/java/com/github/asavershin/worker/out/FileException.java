package com.github.asavershin.worker.out;

public class FileException extends RuntimeException {
    /**
     * Constructs a new FileException with the specified detail message.
     *
     * @param s the detail message
     */
    public FileException(final String s) {
        super(s);
    }
}
