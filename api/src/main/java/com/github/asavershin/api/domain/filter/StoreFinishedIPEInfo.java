package com.github.asavershin.api.domain.filter;

public interface StoreFinishedIPEInfo {
    /**
     * Stores the request id and processed image id.
     *
     * @param requestId The unique identifier for the event.
     * @param processedImageId The unique identifier for the processed image.
     */
    void store(String requestId, String processedImageId);
}
