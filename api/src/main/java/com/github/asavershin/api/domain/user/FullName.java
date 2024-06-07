package com.github.asavershin.api.domain.user;


import com.github.asavershin.api.common.Validator;

import java.util.Objects;

/**
 * A value object representing a user's full name,
 * consisting of a first name and a last name.
 * The length of each name is limited to 20 characters.
 *
 * @param firstname The first name of the user
 * @param lastname  The last name of the user
 */
public record FullName(String firstname, String lastname) {
    /**
     * The maximum length of a first name.
     */
    private static final int MAX_FIRST_NAME_LENGTH = 20;
    /**
     * The maximum length of a last name.
     */
    private static final int MAX_LAST_NAME_LENGTH = 20;

    /**
     * Constructs a new instance of FullName and validates the length of
     * the first and last names.
     */
    public FullName {
        validateName(firstname, MAX_FIRST_NAME_LENGTH);
        validateName(lastname, MAX_LAST_NAME_LENGTH);
    }

    private void validateName(final String name, final int maxLength) {
        Objects.requireNonNull(name, "Firstname must not be null");
        Validator.assertArgumentLength(
                name,
                0,
                MAX_FIRST_NAME_LENGTH,
                "Name must be " + "0-" + maxLength + " in length"
        );
    }
}

