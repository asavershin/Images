package com.github.asavershin.api.domain.user.impl;

import com.github.asavershin.api.common.annotations.Query;
import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.user.GetPartOfImagesForAuthenticatedUser;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementation of the {@link GetPartOfImagesForAuthenticatedUser} interface.
 * This class provides methods to retrieve a part of
 * images for an authenticated user.
 *
 * @author asavershin
 */
@Query
@RequiredArgsConstructor
public class GetPartOfImagesForAuthenticatedUserImpl
        implements GetPartOfImagesForAuthenticatedUser {
    /**
     * The ImageRepository dependency for fetching images.
     */
    private final ImageRepository imageRepository;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public List<Image> get(final UserId userId,
                                 final PartOfResources partOfResources) {
        return imageRepository.findImagesByUserId(userId, partOfResources);
    }
}
