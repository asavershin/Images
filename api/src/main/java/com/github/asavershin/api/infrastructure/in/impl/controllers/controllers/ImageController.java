package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers;

import com.github.asavershin.api.application.in.services.image.ImageService;
import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.GetPartOfImagesForAuthenticatedUser;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.image.GetImagesResponse;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.UISuccessContainer;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.image.UploadImageResponse;
import com.github.asavershin.api.infrastructure.in.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/image")
@Tag(name = "image", description = "Работа с изображениями")
@RequiredArgsConstructor
public class ImageController {

    private final GetPartOfImagesForAuthenticatedUser getImages;
    private final ImageService imageService;

    @GetMapping("/images")
    @Operation(description = "Получить изображения пользователя")
    public GetImagesResponse getImages(@AuthenticationPrincipal CustomUserDetails user,
                                       Long pageNumber,
                                       Long pageSize) {
        return GetImagesResponse.GetImagesResponseFromImages(
                getImages.get(user.authenticatedUser().userId(), new PartOfResources(pageNumber, pageSize))
        );
    }

    @PostMapping
    @Operation(description = "Загрузить новую картинку")
    public UploadImageResponse uploadImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("file") MultipartFile file
    ) {
        return new UploadImageResponse(imageService.storeImage(user.authenticatedUser().userId(), file));
    }

    @DeleteMapping("/{image-id}")
    @Operation(description = "Удалить картинку")
    public UISuccessContainer deleteImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("image-id") String imageId
    ) {
        imageService.deleteImageByImageId(user.authenticatedUser().userId(), new ImageId(UUID.fromString(imageId)));
        return new UISuccessContainer(
                true,
                "Image with id " + imageId +" deleted successfully"
        );
    }

    @GetMapping("/{image-id}")
    @Operation(description = "Получить картинку по id")
    public byte[] downloadImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("image-id") String imageId
    ) {
        return imageService.downloadImage(
                new ImageId(UUID.fromString(imageId)),
                user.authenticatedUser().userId()
        );
    }
}
