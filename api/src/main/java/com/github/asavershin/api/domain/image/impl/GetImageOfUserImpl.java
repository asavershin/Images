package com.github.asavershin.api.domain.image.impl;

import com.github.asavershin.api.common.annotations.DomainService;
import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.image.GetImageOfUser;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class GetImageOfUserImpl extends IsEntityFound implements GetImageOfUser {
    private final ImageRepository imageRepository;
    @Override
    public Image getImageOfUser(UserId userId, ImageId imageId) {
        var image = imageRepository.findImageByImageId(imageId);
        isEntityFound(image, "Image", "Id", imageId.value().toString());
        image.belongsToUser(userId);
        return image;
    }

}
