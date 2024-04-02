package com.github.asavershin.api.application.in.services.image.impl;

import com.github.asavershin.api.application.in.services.image.ImageService;
import com.github.asavershin.api.application.out.MinioService;
import com.github.asavershin.api.common.Validator;
import com.github.asavershin.api.domain.image.*;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final MinioService minioService;
    private final GetImageOfUser getImageOfUser;
    private final DeleteImageOfUser deleteImageOfUser;
    private final StoreImageOfUser storeImageOfUser;
    @Override
    @Transactional
    public ImageId storeImage(UserId userId, MultipartFile multipartFile) {
        log.info("Store image: {}", multipartFile.getOriginalFilename());
        var metaInfo = new MetaInfo(
                ImageNameWithExtension
                        .fromOriginalFileName(multipartFile.getOriginalFilename()),
                multipartFile.getSize()
        );
        log.info("Store image: check on ex");
        var imageId = new ImageId(UUID.fromString(minioService.saveFile(multipartFile)));
        storeImageOfUser.storeImageOfUser(
                new Image(
                        imageId,
                        metaInfo,
                        userId
                )
        );
        return imageId;
    }

    @Override
    @Transactional
    public void deleteImageByImageId(UserId userId, ImageId imageId) {
        deleteImageOfUser.removeImageOfUser(imageId, userId);
        minioService.deleteFiles(List.of(imageId.value().toString()));
    }

    @Override
    public byte[] downloadImage(ImageId imageId, UserId userId) {
        return minioService.getFile(
                getImageOfUser.getImageOfUser(userId, imageId).imageId().value().toString()
        );
    }
}
