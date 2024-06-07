/**
 * A unique identifier for a user.
 *
 * @author asavershin
 */
package com.github.asavershin.api.domain.user;

import java.util.Objects;
import java.util.UUID;

/**
 * A value object representing a unique identifier for a user.
 *
 * @param value The unique identifier for the user.
 */
public record UserId(UUID value) {

    /**
     * Constructs a new instance of {@code UserId} with the provided
     * unique identifier.
     *
     * @param value The unique identifier for the user.
     * @throws NullPointerException if the provided value is null.
     */
    public UserId {
        Objects.requireNonNull(value, "User ID must not be null");
    }

    /**
     * Generates a new unique identifier for a user.
     *
     * @return A new unique identifier for a user.
     */
    public static UserId nextIdentity() {
        return new UserId(UUID.randomUUID());
    }
}
