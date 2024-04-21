package com.github.asavershin.api.domain.filter;

import com.github.asavershin.api.domain.image.ImageId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ImageProcessingInProgress {
    /**
     * result that contains status and imageId.
     */
    private ImageProcessingResult result;
    /**
     * The unique id of the event.
     */
    private RequestId requestId;

    /**
     * Constructs an ImageProcessingInProgress object with the provided image
     * processing event id and processed image id.
     *
     * @param aRequestId       The unique id of the event.
     * @param processedImageId The id of the image that has been processed.
     * @param aOriginalImageId The id of the original image
     * @param status           The status of the event
     */
    public ImageProcessingInProgress(
            final RequestId aRequestId,
            final ImageId processedImageId,
            final Status status,
            final ImageId aOriginalImageId
    ) {
        setRequestId(aRequestId);
        setResult(processedImageId, aOriginalImageId, status);
    }

    /**
     * Constructs an ImageProcessingInProgress when event is finished.
     *
     * @param aRequestId       The unique id of the event.
     * @param processedImageId The id of the image that has been processed.
     * @param status           The status of the event
     */
    public ImageProcessingInProgress(
            final RequestId aRequestId,
            final ImageId processedImageId,
            final Status status
    ) {
        setRequestId(aRequestId);
        setResult(processedImageId, null, status);
    }

    private void setResult(final ImageId processedImageId,
                           final ImageId aOriginalImageId,
                           final Status status) {
        Objects.requireNonNull(status, "Status must not be null");
        if (status == Status.DONE) {
            result = new ImageProcessingResult(status, processedImageId);
            return;
        }
        Objects.requireNonNull(aOriginalImageId, "Original"
                + " image id must not be null when status is WIP");
        result = new ImageProcessingResult(status, aOriginalImageId);
    }

    /**
     * Sets the unique id of the event.
     *
     * @param aRequestId The unique id of the event.
     * @throws NullPointerException if the provided event id is null.
     */
    private void setRequestId(final RequestId aRequestId) {
        Objects.requireNonNull(aRequestId,
                "Image processing event id must not be null");
        this.requestId = aRequestId;
    }
}
