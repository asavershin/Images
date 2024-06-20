package com.github.asavershin.api.domain.filter;

import com.github.asavershin.api.common.Validator;
import com.github.asavershin.api.common.domain.DomainEvent;
import com.github.asavershin.api.domain.image.ImageId;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class ImageProcessingStarted
        implements DomainEvent<FiltersForPublisher> {
    /**
     * Max count of filters for image.
     */
    private static final Integer MAX_FILTERS_COUNT = 32767;
    /**
     * The unique identifier for the ImageProcessingEvent.
     */
    private RequestId requestId;
    /**
     * The unique identifier for the originalImageId.
     */
    private ImageId originalImageId;
    /**
     * The meta information associated with status of processing.
     */
    private final Status status;
    /**
     * Filters that would be applied to the image.
     */
    private List<Filter> filters;

    /**
     * Construct the ImageProcessingStarted.
     *
     * @param aFilters         filters that would be applied to the image
     * @param aOriginalImageId original image id
     */
    public ImageProcessingStarted(
            final List<Filter> aFilters,
            final ImageId aOriginalImageId
    ) {
        setRequestId(RequestId.nextIdentity());
        setOrigImageId(aOriginalImageId);
        setFilters(aFilters);
        status = Status.WIP;
    }

    /**
     * Sets the unique identifier for the ImageProcessingEvent.
     *
     * @param aRequestId the unique identifier for the ImageProcessingEvent.
     * @throws NullPointerException if aRequestId is null
     */
    private void setRequestId(final RequestId aRequestId) {
        Objects.requireNonNull(aRequestId,
                "ImageProcessingEvent id must not be null");
        this.requestId = aRequestId;
    }

    /**
     * Sets the filters for the image processing event.
     *
     * @param aFilters a list of filters to apply to the image
     * @throws IllegalArgumentException if aFilters length is not valid
     * @throws NullPointerException     if aFilters is null
     */
    private void setFilters(final List<Filter> aFilters) {
        Objects.requireNonNull(aFilters, "Filters must not be null");
        Validator.assertCollectionLen(
                aFilters,
                1,
                MAX_FILTERS_COUNT,
                "Must be more than zero filters and less than "
                        + MAX_FILTERS_COUNT);
        this.filters = aFilters;
    }
    /**
     * Sets the unique identifier for the originalImageId.
     *
     * @param aImageId the unique identifier for the originalImageId.
     * @throws NullPointerException if aImageId is null
     */
    private void setOrigImageId(final ImageId aImageId) {
        Objects.requireNonNull(aImageId, "ImageId must not be null");
        this.originalImageId = aImageId;
    }
    /**
     * Publish the ImageProcessingStarted event.
     * @return FiltersForPublisher
     */
    @Override
    public FiltersForPublisher publish() {
        return new FiltersForPublisher(
                originalImageId.value().toString(),
                requestId.value().toString(),
                filters.stream().map(Filter::toString).toList()
        );
    }
}
