package com.github.asavershin.api.domain.image.impl;

import com.github.asavershin.api.common.annotations.Query;
import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.GetPartImagesOfUser;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class GetPartImagesOfUserImpl implements GetPartImagesOfUser {
    /**
     * The ImageRepository dependency for accessing image data.
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
