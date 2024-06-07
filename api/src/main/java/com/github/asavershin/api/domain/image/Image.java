package com.github.asavershin.api.domain.image;

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
    /**
     * The unique identifier for the image.
     */
    private ImageId imageId;

    /**
     * The meta information associated with the image.
     */
    private MetaData metaInfo;

    /**
     * The user who owns the image.
     */
    private UserId userId;

    /**
     * Constructor for creating a new Image object.
     *
     * @param aImageId the unique identifier for the image
     * @param aMetaInfo the meta information associated with the image
     * @param aUserId the user who owns the image
     */
    public Image(final ImageId aImageId,
                 final MetaData aMetaInfo,
                 final UserId aUserId) {
        setImageId(aImageId);
        setMetaInfo(aMetaInfo);
        setUserId(aUserId);
    }

    /**
     * Checks if the given user owns the image.
     *
     * @param aUserId the user to check ownership for
     * @return the same image instance if the user owns the image,
     * otherwise throws a {@link ResourceOwnershipException}
     * @throws ResourceOwnershipException if the image does not belong
     * to the given user
     */
    public Image belongsToUser(final UserId aUserId) {
        if (!this.userId.equals(aUserId)) {
            throw new ResourceOwnershipException(
                    "Image with id " + imageId.value().toString()
                            + " does not belong to user with id "
                            + aUserId.value().toString()
            );
        }
        return this;
    }

    private void setImageId(final ImageId aImageId) {
        Objects.requireNonNull(aImageId, "ImageId must not be null");
        this.imageId = aImageId;
    }

    private void setMetaInfo(final MetaData aMetaInfo) {
        Objects.requireNonNull(aMetaInfo, "MetaInfo must not be null");
        this.metaInfo = aMetaInfo;
    }

    private void setUserId(final UserId aUserId) {
        Objects.requireNonNull(aUserId, "UserId must not be null");
        this.userId = aUserId;
    }
}
