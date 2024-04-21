package com.github.asavershin.api.domain.filter;

/**
 * Enum representing different statuses of events.
 *
 * @author asavershin
 */
public enum Status {
    /**
     * Represents the work in progress status.
     */
    WIP,

    /**
     * Represents the DONE status.
     */
    DONE;

    /**
     * Converts a string representation of an event status
     * to the corresponding enum instance.
     *
     * @param name the string representation of the status name
     * @return the enum instance corresponding to the given
     * string representation
     * @throws IllegalArgumentException if the given string representation
     *                                  does not match any known status name
     */
    public static Status fromString(final String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid status name: " + name
            );
        }
    }
}
