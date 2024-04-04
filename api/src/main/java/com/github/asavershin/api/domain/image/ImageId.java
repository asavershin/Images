package com.github.asavershin.api.domain.image;

import java.util.Objects;
import java.util.UUID;

/**
 * A value object representing an image ID.
 *
 * @param value The unique identifier for the image.
 */
public record ImageId(UUID value) {
    /**
     * Constructs an ImageId instance with the provided UUID.
     *
     * @param value The unique identifier for the image.
     * @throws NullPointerException if the provided value is null.
     */
    public ImageId {
        Objects.requireNonNull(value, "Image ID must not be null");
    }
    /**
     * Generates a new, unique ImageId.
     *
     * @return A new ImageId instance with a randomly generated UUID.
     */
    public static ImageId nextIdentity() {
        return new ImageId(UUID.randomUUID());
    }
}
