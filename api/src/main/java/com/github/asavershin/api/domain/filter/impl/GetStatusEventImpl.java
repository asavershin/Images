package com.github.asavershin.api.domain.filter.impl;

import com.github.asavershin.api.common.annotations.Query;
import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.filter.GetStatusEvent;
import com.github.asavershin.api.domain.filter.RequestId;
import com.github.asavershin.api.domain.filter.ImageProcessingInProgress;
import com.github.asavershin.api.domain.filter.ImageProcessingInProgressRepo;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class GetStatusEventImpl
        extends IsEntityFound
        implements GetStatusEvent {
    /**
     * The repository for {@link ImageProcessingInProgress} entities.
     */
    private final ImageProcessingInProgressRepo repo;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public ImageProcessingInProgress get(
            final RequestId requestId,
            final UserId userId,
            final ImageId imageId) {
        return isEntityFound(
            repo.findByRequestIdImageIdUserId(
                    requestId,
                    imageId,
                    userId
            ),
                "ImageProcessingInProgress",
                "requestId",
                requestId.value().toString()
        );
    }
}
