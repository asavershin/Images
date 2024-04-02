package com.github.asavershin.api.domain.image.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.image.StoreImageOfUser;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class StoreImageOfUserImpl implements StoreImageOfUser {
    private final ImageRepository imageRepository;

    @Override
    public void storeImageOfUser(Image image) {
        imageRepository.save(image);
    }
}
