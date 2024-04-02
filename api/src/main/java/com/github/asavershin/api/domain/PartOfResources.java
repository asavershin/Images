package com.github.asavershin.api.domain;

import com.github.asavershin.api.common.Validator;

import java.util.Objects;

public record PartOfResources(Long pageNumber, Long pageSize) {
    public PartOfResources{
        Objects.requireNonNull(pageNumber, "PageNumber must not be empty");
        Objects.requireNonNull(pageSize, "PageSize must not be empty");
        Validator.assertLongSize(pageNumber, 0L, "PageNumber must not be negative");
        if(pageSize <= 0)
            throw new IllegalArgumentException("PageSize must be positive");
    }
}
