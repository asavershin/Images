package com.github.asavershin.api.domain.image;

import java.util.Objects;
import java.util.UUID;

public record ImageId(UUID value) {
    public ImageId {
        Objects.requireNonNull(value, "Image ID must not be null");
    }

    public static ImageId nextIdentity() {
        return new ImageId(UUID.randomUUID());
    }
}
