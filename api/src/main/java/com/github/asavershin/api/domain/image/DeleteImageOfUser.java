package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.user.UserId;

@FunctionalInterface
public interface DeleteImageOfUser {
    /**
     * Removes the specified image of the specified user.
     *
     * @param imageId the unique identifier of the image to be removed
     * @param userId   the unique identifier of the user who owns the image
     */
    void removeImageOfUser(ImageId imageId, UserId userId);
}
