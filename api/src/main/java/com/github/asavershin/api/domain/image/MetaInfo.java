package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.common.Validator;

import java.util.Objects;

public record MetaInfo(ImageNameWithExtension imageNameWithExtension, Long imageSize) {
    private static final int MAX_IMAGE_NAME = 50;
    /**
     * @param imageName
     * Size is in bytes. 10MB
     */
    private static final long MAX_IMAGE_SIZE = 10485760;

    public MetaInfo {
        validateImageName(imageNameWithExtension);
        validateImageSize(imageSize);
    }

    private void validateImageName(ImageNameWithExtension imageNameWithExtension) {
        Objects.requireNonNull(imageNameWithExtension, "Name and extension must not be null");
    }

    private void validateImageSize(Long imageSize){
        Objects.requireNonNull(imageSize, "Image size must not be null");
        Validator.assertLongSize(imageSize, 0L, MAX_IMAGE_SIZE,
                "Image size must be " + "0-" + MAX_IMAGE_SIZE + " in length");
    }
}
