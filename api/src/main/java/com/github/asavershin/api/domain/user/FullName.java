package com.github.asavershin.api.domain.user;



import com.github.asavershin.api.common.Validator;

import java.util.Objects;

public record FullName(String firstname, String lastname) {
    private static final int MAX_FIRST_NAME_LENGTH = 20;
    private static final int MAX_LAST_NAME_LENGTH = 20;

    public FullName {
        validateName(firstname, MAX_FIRST_NAME_LENGTH);
        validateName(lastname, MAX_LAST_NAME_LENGTH);
    }

    private void validateName(String name, int maxLength) {
        Objects.requireNonNull(name, "Firstname must not be null");
        Validator.assertArgumentLength(name, 0, MAX_FIRST_NAME_LENGTH, "Name must be " + "0-" + maxLength + " in length");
    }
}

