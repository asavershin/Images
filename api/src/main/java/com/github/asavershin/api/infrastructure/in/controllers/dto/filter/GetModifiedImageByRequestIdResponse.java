package com.github.asavershin.api.infrastructure.in.controllers.dto.filter;

import com.github.asavershin.api.domain.filter.ImageProcessingInProgress;
import com.github.asavershin.api.domain.filter.Status;
import com.github.asavershin.api.domain.image.ImageId;
import lombok.Getter;

@Getter
public class GetModifiedImageByRequestIdResponse {
    /**
     * Processed image id if status is done or original image id.
     */
    private String imageId;
    /**
     * Status of the image processing event.
     */
    private String status;

    /**
     * Constructs a {@code GetModifiedImageByRequestIdResponse}
     * object from an {@code ImageProcessingInProgress} event.
     *
     * @param event the {@code ImageProcessingInProgress} event
     *              to construct the response from
     */
    public GetModifiedImageByRequestIdResponse(
            final ImageProcessingInProgress event
    ) {
        setImageId(event.result().imageId());
        setStatus(event.result().status());
    }

    private void setImageId(final ImageId id) {
        this.imageId = id.value().toString();
    }

    private void setStatus(final Status aStatus) {
        this.status = aStatus.toString();
    }
}
