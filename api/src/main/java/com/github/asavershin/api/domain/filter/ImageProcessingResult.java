package com.github.asavershin.api.domain.filter;

import com.github.asavershin.api.domain.image.ImageId;

import java.util.Objects;

public record ImageProcessingResult(Status status, ImageId imageId) {
    /**
     * Construct for image processing result.
     *
     * @param status  of the image processing
     * @param imageId of the processed image if
     *                status is done or original image id
     * @throws NullPointerException     if parameter is null
     */
    public ImageProcessingResult {
        Objects.requireNonNull(status, "Status must not be null");
        Objects.requireNonNull(imageId, "ImageId must not be null");
    }
}
