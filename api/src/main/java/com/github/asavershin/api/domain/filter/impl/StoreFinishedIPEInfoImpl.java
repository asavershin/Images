package com.github.asavershin.api.domain.filter.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.filter.RequestId;
import com.github.asavershin.api.domain.filter.ImageProcessingInProgress;
import com.github.asavershin.api.domain.filter.ImageProcessingInProgressRepo;
import com.github.asavershin.api.domain.filter.Status;
import com.github.asavershin.api.domain.filter.StoreFinishedIPEInfo;
import com.github.asavershin.api.domain.image.ImageId;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class StoreFinishedIPEInfoImpl
        extends IsEntityFound
        implements StoreFinishedIPEInfo {
    /**
     * The repository for {@link ImageProcessingInProgress} entities.
     */
    private final ImageProcessingInProgressRepo finishedRepo;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void store(final String requestId, final String processedImageId) {
        isEntityFound(() -> finishedRepo.save(
                        new ImageProcessingInProgress(
                                RequestId.fromString(requestId),
                                ImageId.fromString(processedImageId),
                                Status.DONE
                        )
                ),
                "ImageProcessingEvent",
                "requestId",
                requestId
        );
    }
}
