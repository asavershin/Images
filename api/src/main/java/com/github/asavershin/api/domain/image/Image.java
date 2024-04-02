package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.ResourceOwnershipException;
import com.github.asavershin.api.domain.user.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
@EqualsAndHashCode
public class Image {
    private ImageId imageId;
    private MetaInfo metaInfo;
    private UserId userId;
    public Image(ImageId imageId, MetaInfo metaInfo, UserId userId){
        setImageId(imageId);
        setMetaInfo(metaInfo);
        setUserId(userId);
    }

    public Image belongsToUser(UserId userId){
        if (!this.userId.equals(userId)){
            throw new ResourceOwnershipException(
                    "Image with id " + imageId.value().toString()+ " does not belong to user with id " + userId.value().toString()
            );
        }
        return this;
    }

    private void setImageId(ImageId imageId) {
        Objects.requireNonNull(imageId, "ImageId must not be null");
        this.imageId = imageId;
    }

    private void setMetaInfo(MetaInfo metaInfo) {
        Objects.requireNonNull(metaInfo, "MetaInfo must not be null");
        this.metaInfo = metaInfo;
    }

    private void setUserId(UserId userId) {
        Objects.requireNonNull(userId, "UserId must not be null");
        this.userId = userId;
    }
}
