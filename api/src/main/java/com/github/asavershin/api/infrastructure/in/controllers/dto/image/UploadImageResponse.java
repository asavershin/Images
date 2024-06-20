package com.github.asavershin.api.infrastructure.in.controllers.dto.image;

import com.github.asavershin.api.domain.image.ImageId;
import lombok.Getter;

@Getter
public class UploadImageResponse {
    /**
     * UUID of the image.
     */
    private final String imageId;
    /**
     * @param aImageId UUID of the image
     */
    public UploadImageResponse(final ImageId aImageId) {
        this.imageId = aImageId.value().toString();
    }
}
