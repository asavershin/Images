package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.image;

import com.github.asavershin.api.domain.image.Image;
import lombok.Getter;

@Getter
public class ImageResponse {
    private String ImageId;
    private String ImageName;
    private Long imageSize;

    private ImageResponse(String ImageId, String ImageName, Long imageSize){
        this.ImageId = ImageId;
        this.ImageName = ImageName;
        this.imageSize = imageSize;
    }

    public static ImageResponse imageResponseFromEntity(Image image){
        return new ImageResponse(
                image.imageId().value().toString(),
                image.metaInfo().imageNameWithExtension().toString(),
                image.metaInfo().imageSize()
        );
    }
}
