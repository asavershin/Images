package com.github.asavershin.api.domain.image.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.image.StoreImageOfUser;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class StoreImageOfUserImpl implements StoreImageOfUser {
    /**
     * The ImageRepository dependency for accessing image data.
     */
    private final ImageRepository imageRepository;
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void storeImageOfUser(final Image image) {
        imageRepository.save(image);
    }
}
