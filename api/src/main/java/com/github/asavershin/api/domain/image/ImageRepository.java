package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.user.UserId;

import java.util.List;

public interface ImageRepository {
    void save(Image image);
    List<Image> findImagesByUserId(UserId userId, PartOfResources partOfResources);

    Image findImageByImageId(ImageId imageId);

    void deleteImageByImageId(Image ImageId);
}
