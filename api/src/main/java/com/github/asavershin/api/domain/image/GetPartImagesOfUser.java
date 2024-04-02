package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.user.UserId;

import java.util.List;

@FunctionalInterface
public interface GetPartImagesOfUser {
    List<Image> get(UserId userId, PartOfResources partOfResources);
}
