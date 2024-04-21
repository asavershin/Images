package com.github.asavershin.api.domain.filter;

import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.UserId;

public interface ImageProcessingInProgressRepo {
    /**
     * Saves an ImageProcessingInProgress event.
     *
     * @param event the ImageProcessingInProgress event to be saved
     */
    void save(ImageProcessingInProgress event);

    /**
     * Finds an ImageProcessingInProgress entity by its RequestId.
     *
     * @param requestId the unique identifier of the image processing task
     * @param userId    the id of the user who owns the image
     * @param imageId   the id of the original image
     * @return the status of the image processing task
     */
    ImageProcessingInProgress findByRequestIdImageIdUserId(
            RequestId requestId,
            ImageId imageId,
            UserId userId

    );
}
