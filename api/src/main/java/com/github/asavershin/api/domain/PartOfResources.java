package com.github.asavershin.api.domain;

import com.github.asavershin.api.common.Validator;

import java.util.Objects;

/**
 * A record representing a part of resources to be fetched from a
 * data source.
 * It contains the page number and the page size for pagination purposes.
 *
 * @param pageNumber The zero-based index of the first record
 *                   to retrieve.
 * @param pageSize   The maximum number of records to retrieve.
 */
public record PartOfResources(Long pageNumber, Long pageSize) {

    /**
     * Constructor for the PartOfResources record.
     *
     * @param pageNumber The zero-based index of the first record
     *                   to retrieve.
     * @param pageSize   The maximum number of records to retrieve.
     * @throws IllegalArgumentException if the pageSize is less than
     *                                  or equal to zero.
     */
    public PartOfResources {
        Objects.requireNonNull(pageNumber,
                "PageNumber must not be empty");
        Objects.requireNonNull(pageSize,
                "PageSize must not be empty");
        Validator.assertLongSize(pageNumber,
                0L, "PageNumber must not be negative");
        Validator.assertLongSize(
                pageSize,
                1L,
                "PageSize must be positive"
        );
    }
}
