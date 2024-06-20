package com.github.asavershin.api.domain.filter;

import java.util.List;
import java.util.Objects;

public record FiltersForPublisher(
        String imageId,
        String requestId,
        List<String> filters) {
    /**
     * Constructs a new instance of {@link FiltersForPublisher}
     * with the provided parameters.
     *
     * @param imageId the unique identifier of the image
     * @param requestId   the requestId associated with the filters
     * @param filters the list of filters to be applied
     * @throws NullPointerException if any of the parameters are null
     */
    public FiltersForPublisher {
        Objects.requireNonNull(imageId, "ImageId must not be null");
        Objects.requireNonNull(requestId, "RequestId must not be null");
        Objects.requireNonNull(filters, "Filters must not be null");
    }
}
