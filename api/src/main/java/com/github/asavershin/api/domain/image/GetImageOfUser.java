package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.user.UserId;

@FunctionalInterface
public interface GetImageOfUser {
    Image getImageOfUser(UserId userId, ImageId imageId);
}
