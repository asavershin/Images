package com.github.asavershin.worker;

public class InvalidWorker extends RuntimeException {
    public InvalidWorker(final String invalidWorker) {
        super(invalidWorker);
    }
}
