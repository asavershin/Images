package com.github.asavershin.api.application.in.services.image;

import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.UserId;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    /**
     * Stores an image for a given user.
     *
     * @param userId the user who owns the image
     * @param multipartFile the image file to be stored
     * @return the unique identifier of the stored image
     */
    ImageId storeImage(UserId userId, MultipartFile multipartFile);
    /**
     * Deletes an image by its unique identifier. Validate that
     * image belongs to user.
     *
     * @param userId the user who owns the image
     * @param imageId the unique identifier of the image to be deleted
     */
    void deleteImageByImageId(UserId userId, ImageId imageId);
    /**
     * Downloads an image by its unique identifier. Validate that
     * image belongs to user.
     *
     * @param imageId the unique identifier of the image to be downloaded
     * @param userId the user who owns the image
     * @return the byte array representation of the image
     */
    byte[] downloadImage(ImageId imageId, UserId userId);
}
