package com.github.asavershin.api.domain.user;


import com.github.asavershin.api.common.Validator;

import java.util.Objects;

public record Credentials(String email, String password) {
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MIN_PASSWORD_LENGTH = 8;

    public Credentials {
        validateEmail(email);
        validatePassword(password);
    }

    private void validateEmail(String email) {
        Objects.requireNonNull(email, "Email must not be null");
        Validator.assertArgumentLength(email,
                MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH,
                "Email must be " + MIN_EMAIL_LENGTH + "-" + MAX_EMAIL_LENGTH + " in length");

        Validator.assertStringFormat(email, "[A-Za-z0-9_.+-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9.-]+",
                "Email is not in the correct format");
    }

    private void validatePassword(String password) {
        Objects.requireNonNull(password, "Password must not be null");
        Validator.assertArgumentLength(password, MIN_PASSWORD_LENGTH, "Password must be greater than " + MIN_PASSWORD_LENGTH);
    }
}

