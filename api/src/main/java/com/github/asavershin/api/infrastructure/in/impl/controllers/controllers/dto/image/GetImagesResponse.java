package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.image;

import com.github.asavershin.api.domain.image.Image;
import lombok.Getter;

import java.util.List;

@Getter
public final class GetImagesResponse {
    /**
     * List of metadata of images.
     */
    private final List<ImageResponse> images;

    private GetImagesResponse(final List<ImageResponse> aImages) {
        this.images = aImages;
    }

    /**
     * Static fabric that map List of images to list of DTO.
     *
     * @param images List of images from domain
     * @return Instance of GetImagesResponse
     */
    public static GetImagesResponse getImagesResponseFromImages(
            final List<Image> images
    ) {
        return new GetImagesResponse(
                images.stream()
                        .map(ImageResponse::imageResponseFromEntity).toList()
        );
    }
}
