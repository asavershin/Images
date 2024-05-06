package com.github.asavershin.images.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * A value object representing an Image Processing Event ID.
 *
 * @param value The unique identifier for the Image Processing Event.
 */
public record RequestId(UUID value) {
    /**
     * Constructs an requestId instance with the provided UUID.
     *
     * @param value The unique identifier for the Image Processing Event.
     * @throws NullPointerException if the provided value is null.
     */
    public RequestId {
        Objects.requireNonNull(
                value,
                "Image Processing Event ID must not be null"
        );
    }

    /**
     * Generates a new, unique requestId.
     *
     * @return A new requestId instance with a randomly generated UUID.
     */
    public static RequestId nextIdentity() {
        return new RequestId(UUID.randomUUID());
    }

    /**
     * Parses a string into a {@link RequestId} instance.
     *
     * @param id The string representation of the {@link RequestId}.
     * @return A new {@link RequestId} instance with the parsed UUID.
     * @throws IllegalArgumentException if the provided string is not
     *                                  a valid UUID.
     */
    public static RequestId fromString(final String id) {
        Objects.requireNonNull(id, "requestId must not be null");
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid requestId: "
                    + ex.getMessage());
        }
        return new RequestId(uuid);
    }
}
