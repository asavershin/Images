package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.common.Validator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@EqualsAndHashCode
public final class ImageNameWithExtension {

    /**
     * The maximum length of an image name.
     */
    private static final int MAX_IMAGE_NAME = 50;

    /**
     * The image name.
     */
    private final String imageName;

    /**
     * The image extension.
     */
    private final ImageExtension imageExtension;

    /**
     * Constructs an {@code ImageNameWithExtension} object.
     *
     * @param aImageName the image name without last extension
     * @param aImageExt  the image extension
     */
    private ImageNameWithExtension(final String aImageName,
                                   final ImageExtension aImageExt) {
        this.imageName = aImageName;
        this.imageExtension = aImageExt;
    }
    /**
     * Creates an {@code ImageNameWithExtension}
     * object from an original file name.
     *
     * @param originalFileName the original file name
     * @return the created {@code ImageNameWithExtension} object
     */
    public static ImageNameWithExtension fromOriginalFileName(
            final String originalFileName
    ) {
        notNullValidate(originalFileName);
        String[] parts = originalFileName.split("\\.");
        Validator.assertArrayLength(parts, 2, "Incorrect image format");
        var extension = ImageExtension
                .fromString("." + parts[parts.length - 1]);
        var imageName = String
                .join(".", Arrays.copyOfRange(parts, 0, parts.length - 1));
        lengthNameValidate(imageName);

        return new ImageNameWithExtension(imageName, extension);
    }

    /**
     * Creates an {@code ImageNameWithExtension}
     * object that founded in repository from an image name and an extension.
     *
     * @param imageName the image name
     * @param extension the image extension
     * @return the created {@code ImageNameWithExtension} object
     */
    public static ImageNameWithExtension founded(
            final String imageName,
            final String extension) {
        notNullValidate(imageName);
        lengthNameValidate(imageName);
        return new ImageNameWithExtension(imageName,
                ImageExtension.fromString(extension));
    }

    private static void notNullValidate(final String name) {
        Objects.requireNonNull(name, "ImageName must not be null");
    }

    private static void lengthNameValidate(final String name) {
        Validator.assertArgumentLength(name, 0, MAX_IMAGE_NAME,
                "ImageName must be " + 0 + "-" + MAX_IMAGE_NAME + " in length");
    }

    @Override
    public String toString() {
        return imageName + imageExtension;
    }
}
