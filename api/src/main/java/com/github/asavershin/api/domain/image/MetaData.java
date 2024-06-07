package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.common.Validator;

import java.util.Objects;

public record MetaData(ImageNameWithExtension imageNameWithExtension,
                       Long imageSize) {
    /**
     * The maximum length of an image name.
     */
    private static final int MAX_IMAGE_NAME = 50;
    /**
     * Size is in bytes. 10MB
     */
    private static final long MAX_IMAGE_SIZE = 10485760;

    /**
     * Constructs a new instance of MetaInfo
     * and validates the provided image name and size.
     */
    public MetaData {
        validateImageName(imageNameWithExtension);
        validateImageSize(imageSize);
    }

    private void validateImageName(
            final ImageNameWithExtension aImageNameWithExtension
    ) {
        Objects.requireNonNull(aImageNameWithExtension,
                "Name and extension must not be null");
        Validator.assertArgumentLength(aImageNameWithExtension.toString(),
                0, MAX_IMAGE_NAME,
                "Image name must be "
                        + "0-" + MAX_IMAGE_NAME + " in length");
    }

    private void validateImageSize(final Long aImageSize) {
        Objects.requireNonNull(aImageSize, "Image size must not be null");
        Validator.assertLongSize(aImageSize, 0L, MAX_IMAGE_SIZE,
                "Image size must be " + "0-" + MAX_IMAGE_SIZE);
    }
}
