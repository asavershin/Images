package com.github.asavershin.api.domain.image.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.image.DeleteImageOfUser;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class DeleteImageOfUserImpl extends IsEntityFound
        implements DeleteImageOfUser {
    /**
     * The ImageRepository dependency for accessing image data.
     */
    private final ImageRepository imageRepository;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void removeImageOfUser(
            final ImageId imageId,
            final UserId userId
    ) {
        var image = imageRepository.findImageByImageId(imageId);
        isEntityFound(image, "Image", "Id", imageId.value().toString());
        image.belongsToUser(userId);
        imageRepository.deleteImageByImageId(
                imageRepository.findImageByImageId(imageId)
        );
    }
}
