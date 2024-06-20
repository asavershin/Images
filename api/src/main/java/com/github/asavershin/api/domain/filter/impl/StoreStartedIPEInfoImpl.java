package com.github.asavershin.api.domain.filter.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.filter.ImageProcessingStarted;
import com.github.asavershin.api.domain.filter.ImageProcessingStartedRepo;
import com.github.asavershin.api.domain.filter.StoreStartedIPEInfo;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class StoreStartedIPEInfoImpl
        extends IsEntityFound
        implements StoreStartedIPEInfo {
    /**
     * The repository for managing images.
     */
    private final ImageRepository imageRepo;
    /**
     * The repository for managing {@link ImageProcessingStarted} events.
     */
    private final ImageProcessingStartedRepo eventRepo;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void store(final ImageProcessingStarted startedEvent,
                      final UserId userId) {
        var image = imageRepo.findImageByImageId(
                startedEvent.originalImageId()
        );
        isEntityFound(image, "Image", "ImageId",
                startedEvent.originalImageId().value().toString());
        image.belongsToUser(userId);
        eventRepo.save(startedEvent);
    }
}
