package com.github.asavershin.api.domain.image;

@FunctionalInterface
public interface StoreImageOfUser {
    /**
     * Stores the provided image of a user.
     *
     * @param image The image to be stored.
     */
    void storeImageOfUser(Image image);
}
