package com.github.asavershin.api.domain.user.impl;

import com.github.asavershin.api.common.annotations.Query;
import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.user.GetPartOfImagesForAuthenticatedUser;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class GetPartOfImagesForAuthenticatedUserImpl implements GetPartOfImagesForAuthenticatedUser {
    private final ImageRepository imageRepository;
    @Override
    public List<Image> get(UserId userId, PartOfResources partOfResources) {
        return imageRepository.findImagesByUserId(userId, partOfResources);
    }
}
