package com.github.asavershin.api.domain.user;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.Image;

import java.util.List;

@FunctionalInterface
public interface GetPartOfImagesForAuthenticatedUser {
    public List<Image> get(UserId userId, PartOfResources partOfResources);
}
