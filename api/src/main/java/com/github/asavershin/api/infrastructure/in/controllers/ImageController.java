package com.github.asavershin.api.infrastructure.in.controllers;

import com.github.asavershin.api.application.in.services.image.ImageService;
import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.filter.Filter;
import com.github.asavershin.api.domain.filter.GetStatusEvent;
import com.github.asavershin.api.domain.filter.ImageProcessingStarted;
import com.github.asavershin.api.domain.filter.RequestId;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.GetPartOfImagesForAuthenticatedUser;
import com.github.asavershin.api.infrastructure.in.controllers.dto.filter.ApplyImageFiltersResponse;
import com.github.asavershin.api.infrastructure.in.controllers.dto.filter.GetModifiedImageByRequestIdResponse;
import com.github.asavershin.api.infrastructure.in.controllers.dto.image.GetImagesResponse;
import com.github.asavershin.api.infrastructure.in.controllers.dto.UISuccessContainer;
import com.github.asavershin.api.infrastructure.in.controllers.dto.image.UploadImageResponse;
import com.github.asavershin.api.infrastructure.in.security.CustomUserDetails;
import com.github.asavershin.api.infrastructure.out.producers.KafkaProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/image")
@Tag(name = "image", description = "Работа с изображениями")
@RequiredArgsConstructor
public class ImageController {
    /**
     * Domain query that allows you take images of specific user.
     */
    private final GetPartOfImagesForAuthenticatedUser getImages;
    /**
     * Application service that contains logic for images get, post, delete.
     * It contains some others dependencies for databases and jwt service.
     */
    private final ImageService imageService;
    /**
     * Kafka producer for sending image processing events.
     */
    private final KafkaProducer producer;
    /**
     * Service for tracking the status of image processing events.
     */
    private final GetStatusEvent statusEvent;

    /**
     * Not final to allows Spring use proxy.
     *
     * @param user       Is param that injects by spring and contains
     *                   current authenticated spring user.
     * @param pageNumber Page number for pagination in DB
     * @param pageSize   Page size for pagination in DB
     * @return List of images for specific user.
     */
    @GetMapping("/images")
    @Operation(description = "Получить изображения пользователя")
    public GetImagesResponse getImages(
            final @AuthenticationPrincipal CustomUserDetails user,
            final Long pageNumber,
            final Long pageSize
    ) {
        return GetImagesResponse.getImagesResponseFromImages(
                getImages.get(user.authenticatedUser().userId(),
                        new PartOfResources(pageNumber, pageSize)
                )
        );
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param user Is param that injects by spring and contains
     *             current authenticated spring user.
     * @param file Is image with data and metadata.
     * @return ImageId of the new image
     */
    @PostMapping
    @Operation(description = "Загрузить новую картинку")
    public UploadImageResponse uploadImage(
            final @AuthenticationPrincipal CustomUserDetails user,
            final @RequestPart("file") MultipartFile file
    ) {
        return new UploadImageResponse(
                imageService.storeImage(
                        user.authenticatedUser().userId(),
                        file
                )
        );
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param user    Is param that injects by spring and contains
     *                current authenticated spring user.
     * @param imageId The id of the image
     * @return The response that contains status of response(true, false)
     * and message for user about successful.
     */
    @DeleteMapping("/{image-id}")
    @Operation(description = "Удалить картинку")
    public UISuccessContainer deleteImage(
            final @AuthenticationPrincipal CustomUserDetails user,
            final @PathVariable("image-id") String imageId
    ) {
        imageService.deleteImageByImageId(
                user.authenticatedUser().userId(),
                new ImageId(UUID.fromString(imageId))
        );
        return new UISuccessContainer(
                true,
                "Image with id " + imageId + " deleted successfully"
        );
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param user    Is param that injects by spring and contains
     *                current authenticated spring user.
     * @param imageId The id of the image
     * @return bytes of the image
     */
    @GetMapping("/{image-id}")
    @Operation(description = "Получить картинку по id")
    public byte[] downloadImage(
            final @AuthenticationPrincipal CustomUserDetails user,
            final @PathVariable("image-id") String imageId
    ) {
        return imageService.downloadImage(
                new ImageId(UUID.fromString(imageId)),
                user.authenticatedUser().userId()
        );
    }
    /**
     * This start image processing event by authenticated user to his image.
     *
     * @param user      Is param that injects by spring and contains
     *                  current authenticated spring user.
     * @param imageId The ID of the image that will be processed.
     * @param filters The list of filters that will be applied to the image.
     * @return request id
     */
    @PostMapping("/{image-id}/filters")
    @Operation(description = "Применение указанных фильтров к изображению")
    public ApplyImageFiltersResponse applyImageFilters(
            final @AuthenticationPrincipal CustomUserDetails user,
            final @PathVariable("image-id") String imageId,
            final @RequestParam List<String> filters) {
        var event = new ImageProcessingStarted(
                filters.stream()
                        .map(Filter::fromString)
                        .toList(),
                new ImageId(UUID.fromString(imageId))
        );
        producer.send(event, user.authenticatedUser().userId());
        return new ApplyImageFiltersResponse(
                event.requestId().value().toString()
        );
    }

    /**
     * This method retrieves the ID of the modified image
     * based on the provided request ID and image ID.
     *
     * @param user      The authenticated user whose images are being accessed.
     * @param imageId   The ID of the image to which the filter was applied.
     * @param requestId The ID of the request that initiated
     *                  the image processing.
     * @return A response containing the ID of the modified image.
     */
    @GetMapping("/image/{image-id}/filters/{request-id}")
    @Operation(description = "Получение ИД измененного файла по ИД запроса")
    public GetModifiedImageByRequestIdResponse
    getModifiedImageByRequestIdResponse(
            final @AuthenticationPrincipal CustomUserDetails user,
            final @PathVariable("image-id") String imageId,
            final @PathVariable("request-id") String requestId
    ) {
        return new GetModifiedImageByRequestIdResponse(
                statusEvent
                        .get(
                                RequestId.fromString(requestId),
                                user.authenticatedUser().userId(),
                                ImageId.fromString(imageId)
                        )
        );
    }
}
