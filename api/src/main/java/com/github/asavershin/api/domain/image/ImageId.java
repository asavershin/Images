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

    /**
     * Parses a string into an ImageId.
     *
     * @param id The string representation of the ImageId.
     * @return A new ImageId instance with the parsed UUID.
     * @throws IllegalArgumentException if the provided string
     *                                  is not a valid UUID.
     */
    public static ImageId fromString(final String id) {
        Objects.requireNonNull(id, "ImageId must not be null");
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid ImageId: "
                    + ex.getMessage());
        }
        return new ImageId(uuid);
    }
}
