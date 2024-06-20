package com.github.asavershin.api.domain.filter;

public interface ImageProcessingStartedRepo {
    /**
     * Used to store a new image processing event.
     * @param event the image processing started aggregate
     */
    void save(ImageProcessingStarted event);
}
