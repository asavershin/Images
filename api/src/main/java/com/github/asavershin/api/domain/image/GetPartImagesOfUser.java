package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.user.UserId;

import java.util.List;

@FunctionalInterface
public interface GetPartImagesOfUser {
    /**
     * This interface represents a function that retrieves a list of
     * images for a specific user and part of resources.
     *
     * @param userId the unique identifier of the user
     * @param partOfResources pagination
     * @return a list of images belonging to the specified
     * user and part of resources as pagination
     */
    List<Image> get(UserId userId, PartOfResources partOfResources);
}
