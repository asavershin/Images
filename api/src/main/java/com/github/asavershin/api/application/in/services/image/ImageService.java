package com.github.asavershin.api.application.in.services.image;

import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.UserId;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageId storeImage(UserId userId, MultipartFile multipartFile);
    void deleteImageByImageId(UserId userId, ImageId imageId);
    byte[] downloadImage(ImageId imageId, UserId userId);
}
