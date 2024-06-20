package com.github.asavershin.api.domain.filter;

import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.UserId;

@FunctionalInterface
public interface GetStatusEvent {
    /**
     * Retrieves the status of an image processing task with
     * the given identifier.
     *
     * @param id the unique identifier of the image processing task
     * @param userId the id of the user who owns the image
     * @param imageId the id of the original image
     * @return the status of the image processing task
     */
    ImageProcessingInProgress get(
            RequestId id,
            UserId userId,
            ImageId imageId);
}
