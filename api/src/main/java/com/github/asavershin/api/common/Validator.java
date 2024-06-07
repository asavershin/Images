package com.github.asavershin.api.common;

import java.util.Objects;

/**
 * A utility class for performing basic validation checks.
 *
 * @author asavershin
 */
public abstract class Validator {

    /**
     * Asserts that the given string has a length within
     * the specified range.
     *
     * @param aString the string to validate
     * @param aMinimum the minimum length
     * @param aMaximum the maximum length
     * @param aMessage the message to throw if the length is invalid
     * @throws IllegalArgumentException if the length of the string
     * is less than {@code aMinimum} or greater than {@code aMaximum}
     */
    public static void assertArgumentLength(final String aString,
                                            final int aMinimum,
                                            final int aMaximum,
                                            final String aMessage) {
        int length = aString.length();
        if (length < aMinimum || length > aMaximum) {
            throw new IllegalArgumentException(aMessage);
        }
    }
    /**
     * Asserts that the given string has a length greater than or equal
     * to the specified minimum.
     *
     * @param aString the string to validate
     * @param aMinimum the minimum length
     * @param aMessage the message to throw if the length is invalid
     * @throws IllegalArgumentException if the length of the string is
     * less than {@code aMinimum}
     */
    public static void assertArgumentLength(final String aString,
                                            final int aMinimum,
                                            final String aMessage) {
        int length = aString.length();
        if (length < aMinimum) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    /**
     * Asserts that the given string matches the specified regular
     * expression.
     *
     * @param email the string to validate
     * @param regex the regular expression to match against
     * @param aMessage the message to throw if the string does not
     *                match the regular expression
     * @throws IllegalArgumentException if the string does not
     * match the regular expression
     */
    public static void assertStringFormat(final String email,
                                          final String regex,
                                          final String aMessage) {
        if (!email.matches(regex)) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    /**
     * Asserts that the given array has a length greater than or
     * equal to the specified minimum.
     *
     * @param array the array to validate
     * @param minLength the minimum length
     * @param aMessage the message to throw if the length of the
     *                array is less than {@code minLength}
     * @throws IllegalArgumentException if the length of the array
     * is less than {@code minLength}
     */
    public static void assertArrayLength(final Object[] array,
                                         final Integer minLength,
                                         final String aMessage) {
        if (array.length < minLength) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    /**
     * Asserts that the given value is within the specified range.
     *
     * @param value the value to validate
     * @param minLength the minimum length
     * @param maxLength the maximum length
     * @param aMessage the message to throw if the value is outside
     *                the specified range
     * @throws IllegalArgumentException if the value is less than
     * {@code minLength} or greater than {@code maxLength}
     */
    public static void assertLongSize(final Long value,
                                      final Long minLength,
                                      final Long maxLength,
                                      final String aMessage) {
        if (value < minLength || value > maxLength) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    /**
     * Asserts that the given value is greater than or equal to the
     * specified minimum.
     *
     * @param value the value to validate
     * @param minLength the minimum length
     * @param aMessage the message to throw if the value is less
     *                than {@code minLength}
     * @throws IllegalArgumentException if the value is less
     * than {@code minLength}
     */
    public static void assertLongSize(final Long value,
                                      final Long minLength,
                                      final String aMessage) {
        if (value < minLength) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    /**
     * Asserts that the given object is not null.
     *
     * @param object the object to validate
     * @param aMessage the message to throw if the object is null
     * @throws IllegalArgumentException if the object is null
     */
    public static void assertNotFound(final Object object,
                                      final String aMessage) {
        if (Objects.isNull(object)) {
            throw new NotFoundException(aMessage);
        }
    }
}
