package com.github.asavershin.api.domain.user;

import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "User ID must not be null");
    }

    public static UserId nextIdentity() {
        return new UserId(UUID.randomUUID());
    }
}
