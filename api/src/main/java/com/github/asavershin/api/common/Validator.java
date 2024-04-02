package com.github.asavershin.api.common;

import java.util.Objects;

public class Validator {
    public static void assertArgumentLength(String aString, int aMinimum, int aMaximum, String aMessage) {
        int length = aString.length();
        if (length < aMinimum || length > aMaximum) {
            throw new IllegalArgumentException(aMessage);
        }
    }
    public static void assertArgumentLength(String aString, int aMinimum, String aMessage) {
        int length = aString.length();
        if (length < aMinimum) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    public static void assertStringFormat(String email, String regex, String aMessage) {
        if (!email.matches(regex)) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    public static void assertArrayLength(Object[] array, Integer minLength, String aMessage) {
        if (array.length < minLength) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    public static void assertLongSize(Long value, Long minLength, Long maxLength, String aMessage) {
        if (value < minLength || value > maxLength) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    public static void assertLongSize(Long value, Long minLength, String aMessage) {
        if (value < minLength) {
            throw new IllegalArgumentException(aMessage);
        }
    }

    public static void assertNotFound(Object object, String aMessage) {
        if (Objects.isNull(object)) {
            throw new NotFoundException(aMessage);
        }
    }
}
