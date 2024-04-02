package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.image;

import com.github.asavershin.api.domain.image.ImageId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UploadImageResponse {
    private String imageId;
    public UploadImageResponse(ImageId imageId){
        this.imageId = imageId.value().toString();
    }
}
