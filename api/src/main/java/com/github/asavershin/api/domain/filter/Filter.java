package com.github.asavershin.api.domain.filter;


/**
 * Enum representing different filters for images.
 *
 * @author asavershin
 */
public enum Filter {
    /**
     * Represents the REVERS_COLORS filter.
     */
    ROTATE,

    /**
     * Represents the CROP filter.
     */
    BLACKWHITE;

    /**
     * Converts a string representation of a filter name string
     * to the corresponding enum instance.
     *
     * @param name the string representation of the filter name
     * @return the enum instance corresponding to the given
     * string representation
     * @throws IllegalArgumentException if the given string representation
     * does not match any known filters
     */
    public static Filter fromString(final String name) {
        try {
            return Filter.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid filter name: " + name
            );
        }
    }
}
