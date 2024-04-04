package com.github.asavershin.api.domain.image;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Enum representing different image file extensions.
 *
 * @author asavershin
 */
public enum ImageExtension {
    /**
     * Represents the JPG image file extension.
     */
    JPG(".jpg"),

    /**
     * Represents the PNG image file extension.
     */
    PNG(".png"),

    /**
     * Represents the JPEG image file extension.
     */
    JPEG(".jpeg");

    /**
     * Represents the string image file extension
     * in format .extension.
     */
    private final String extension;

    ImageExtension(final String aExtension) {
        this.extension = aExtension;
    }

    @Override
    public String toString() {
        return extension;
    }

    /**
     * Map that stores a string representation of an extension in keys,
     * and their corresponding ENUM in values.
     */
    private static final Map<String, ImageExtension> STRING_TO_ENUM
            = Stream.of(values()).collect(toMap(Object::toString, e -> e));

    /**
     * Converts a string representation of an image extension
     * to the corresponding enum instance.
     *
     * @param extension the string representation of the image extension
     * @return the enum instance corresponding to the given
     * string representation
     * @throws IllegalArgumentException if the given string representation
     * does not match any known image extension
     */
    public static ImageExtension fromString(final String extension) {
        var imageExtension = STRING_TO_ENUM.get(extension);
        if (imageExtension == null) {
            throw new IllegalArgumentException(
                    "Invalid extension: " + extension
            );
        }
        return imageExtension;
    }
}
