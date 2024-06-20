package com.github.asavershin.api.domain.filter;

import com.github.asavershin.api.domain.user.UserId;

public interface StoreStartedIPEInfo {
    /**
     * Stores the {@link ImageProcessingStarted} event
     * along with the {@link UserId}
     * of the user who initiated the event.
     *
     * @param startedEvent The {@link ImageProcessingStarted}
     *                     event to be stored.
     * @param userId       The {@link UserId} of the user who
     *                     initiated the event.
     */
    void store(
            ImageProcessingStarted startedEvent,
            UserId userId
    );
}
