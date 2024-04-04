package com.github.asavershin.api.application.in.services.image.impl;

import com.github.asavershin.api.application.in.services.image.ImageService;
import com.github.asavershin.api.application.out.MinioService;
import com.github.asavershin.api.domain.image.DeleteImageOfUser;
import com.github.asavershin.api.domain.image.GetImageOfUser;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.ImageNameWithExtension;
import com.github.asavershin.api.domain.image.MetaData;
import com.github.asavershin.api.domain.image.StoreImageOfUser;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService, Serializable {
    /**
     * The MinioService is used to interact with the Minio storage service.
     */
    private final MinioService minioService;

    /**
     * The GetImageOfUser service is used to retrieve an image
     * by its ID and user ID.
     */
    private final GetImageOfUser getImageOfUser;

    /**
     * The DeleteImageOfUser service is used to delete an image
     * by its ID and user ID.
     */
    private final DeleteImageOfUser deleteImageOfUser;

    /**
     * The StoreImageOfUser service is used to store an image
     * with its metadata and associate it with a user.
     */
    private final StoreImageOfUser storeImageOfUser;

    /**
     * Method not marked as final to allow Spring
     * to create a proxy first for transactional purposes.
     * @param userId the user who owns the image
     * @param multipartFile the image file to be stored
     * @return ID of new stored image
     */
    @Override
    @Transactional
    public ImageId storeImage(final UserId userId,
                                    final MultipartFile multipartFile) {
        var metaInfo = new MetaData(
                ImageNameWithExtension
                        .fromOriginalFileName(
                                multipartFile.getOriginalFilename()
                        ),
                multipartFile.getSize()
        );
        // TODO Why minio service store before postgreSQL?
        var imageId = new ImageId(
                UUID.fromString(minioService.saveFile(multipartFile))
        );
        storeImageOfUser.storeImageOfUser(
                new Image(
                        imageId,
                        metaInfo,
                        userId
                )
        );
        return imageId;
    }

    /**
     * Method not marked as final to allow Spring
     *      * to create a proxy first for transactional purposes.
     * @param userId the user who owns the image
     * @param imageId the unique identifier of the image to be deleted
     */
    @Override
    @Transactional
    public void deleteImageByImageId(final UserId userId,
                                           final ImageId imageId) {
        deleteImageOfUser.removeImageOfUser(imageId, userId);
        minioService.deleteFiles(List.of(imageId.value().toString()));
    }

    /**
     * Method not marked as final to allow Spring make CGLIB proxy.
     * @param imageId the unique identifier of the image to be downloaded
     * @param userId the user who owns the image
     * @return bytes of the image
     */
    @Override
    public byte[] downloadImage(final ImageId imageId,
                                      final UserId userId) {
        return minioService.getFile(
                getImageOfUser.getImageOfUser(
                                userId,
                                imageId
                        )
                        .imageId().value().toString()
        );
    }
}
