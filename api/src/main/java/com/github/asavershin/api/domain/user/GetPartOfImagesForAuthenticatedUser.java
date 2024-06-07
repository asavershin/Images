package com.github.asavershin.api.domain.user;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.Image;

import java.util.List;

/**
 * This interface represents a function that retrieves a list of images
 * for a given authenticated user and a specific part of resources.
 *
 */
@FunctionalInterface
public interface GetPartOfImagesForAuthenticatedUser {
    /**
     * Retrieves a list of images for a given authenticated user and
     * a specific part of resources.
     *
     * @param userId the unique identifier of the authenticated user
     * @param partOfResources the specific part of resources
     *                        for which the images are requested
     * @return a list of images associated with the specified
     * user and part of resources
     */
    List<Image> get(UserId userId, PartOfResources partOfResources);
}
