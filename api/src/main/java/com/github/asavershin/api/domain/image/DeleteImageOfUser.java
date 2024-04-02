package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.user.UserId;

@FunctionalInterface
public interface DeleteImageOfUser {
    void removeImageOfUser(ImageId imageId, UserId userId);
}
