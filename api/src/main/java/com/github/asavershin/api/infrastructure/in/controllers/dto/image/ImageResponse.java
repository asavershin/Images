package com.github.asavershin.api.infrastructure.in.controllers.dto.image;

import com.github.asavershin.api.domain.image.Image;
import lombok.Getter;

@Getter
public final class ImageResponse {
    /**
     * UUID of the image.
     */
    private final String imageId;
    /**
     * Image name in format "name.extension".
     */
    private final String imageName;
    /**
     * Count of image bytes.
     */
    private final Long imageSize;

    private ImageResponse(final String aImageId,
                          final String aImageName,
                          final Long aImageSize) {
        this.imageId = aImageId;
        this.imageName = aImageName;
        this.imageSize = aImageSize;
    }

    /**
     * Static fabric for creating ImageResponse from image entity.
     *
     * @param image is domain entity
     * @return DTO
     */
    public static ImageResponse imageResponseFromEntity(final Image image) {
        return new ImageResponse(
                image.imageId().value().toString(),
                image.metaInfo().imageNameWithExtension().toString(),
                image.metaInfo().imageSize()
        );
    }
}
