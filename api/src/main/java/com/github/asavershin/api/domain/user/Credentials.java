package com.github.asavershin.api.domain.user;


import com.github.asavershin.api.common.Validator;

import java.util.Objects;

public record Credentials(String email, String password) {
    /**
     * The maximum length of an email.
     */
    private static final int MAX_EMAIL_LENGTH = 50;

    /**
     * The minimum length of an email.
     */
    private static final int MIN_EMAIL_LENGTH = 5;

    /**
     * The minimum length of a password.
     */
    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Constructs a new instance of Credentials.
     * Validates the email and password before creating the instance.
     */
    public Credentials {
        validateEmail(email);
        validatePassword(password);
    }

    private void validateEmail(final String aEmail) {
        Objects.requireNonNull(aEmail, "Email must not be null");
        Validator.assertArgumentLength(aEmail,
                MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH,
                "Email must be "
                        + MIN_EMAIL_LENGTH + "-"
                        + MAX_EMAIL_LENGTH + " in length");

        Validator.assertStringFormat(aEmail,
                "[A-Za-z0-9_.+-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9.-]+",
                "Email is not in the correct format");
    }

    private void validatePassword(final String aPassword) {
        Objects.requireNonNull(aPassword, "Password must not be null");
        Validator.assertArgumentLength(aPassword,
                MIN_PASSWORD_LENGTH,
                "Password must be greater than " + MIN_PASSWORD_LENGTH);
    }
}

