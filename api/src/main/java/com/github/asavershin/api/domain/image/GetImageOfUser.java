package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.user.UserId;

@FunctionalInterface
public interface GetImageOfUser {
    /**
     * Method that retrieves an image of a user.
     *
     * @param userId the unique identifier of the user
     * @param imageId the unique identifier of the image
     * @return the image of the specified user
     */
    Image getImageOfUser(UserId userId, ImageId imageId);
}
