package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.user.UserId;

import java.util.List;

public interface ImageRepository {
    /**
     * Saves an Image object to the database.
     *
     * @param image the Image object to be saved
     */
    void save(Image image);
    /**
     * Finds all Images associated with the given UserId and PartOfResources.
     *
     * @param userId the UserId of the user whose images are to be retrieved
     * @param partOfResources the PartOfResources to filter the images by
     * @return a list of Images associated with the given UserId
     * and PartOfResources
     */
    List<Image> findImagesByUserId(UserId userId,
                                   PartOfResources partOfResources);

    /**
     * Finds an Image by its ImageId.
     *
     * @param imageId the ImageId of the Image to be retrieved
     * @return the Image object with the given ImageId, or null if not found
     */
    Image findImageByImageId(ImageId imageId);

    /**
     * Deletes an Image by its ImageId.
     *
     * @param imageId the ImageId of the Image to be deleted
     */
    void deleteImageByImageId(Image imageId);
}
