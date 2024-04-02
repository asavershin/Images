package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.image;

import com.github.asavershin.api.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetImagesResponse {
    private List<ImageResponse> images;
    private GetImagesResponse(List<ImageResponse> images){
        this.images = images;
    }
    public static GetImagesResponse GetImagesResponseFromImages(List<Image> images) {
        return new GetImagesResponse(
                images.stream()
                .map(ImageResponse::imageResponseFromEntity).toList()
        );
    }
}
