package com.github.asavershin.api.integrations;

import asavershin.generated.package_.tables.Image;
import asavershin.generated.package_.tables.Users;
import com.github.asavershin.api.application.in.services.image.ImageService;
import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.common.ImageHelper;
import com.github.asavershin.api.common.UserHelper;
import com.github.asavershin.api.config.properties.MinIOProperties;
import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.ResourceOwnershipException;
import com.github.asavershin.api.domain.image.GetPartImagesOfUser;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.user.AuthenticatedUserRepository;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class ImageLogicTest extends AbstractTest{
    @Autowired
    private DSLContext dslContext;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RegisterNewUser registerNewUser;

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Autowired
    private GetPartImagesOfUser getPartImagesOfUser;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIOProperties minioProperties;

    @BeforeEach
    public void clearDB(){
        dslContext.delete(Users.USERS).execute();
        dslContext.delete(Image.IMAGE).execute();
        tokenRepository.deleteAllTokensByUserEmail(UserHelper.credentials1().email());
        tokenRepository.deleteAllTokensByUserEmail(UserHelper.credentials1().email());

        try {
            Iterable<Result<Item>> files = minioClient.listObjects(ListObjectsArgs.builder().bucket(minioProperties.getBucket()).build());

            for (var file : files) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(file.get().objectName())
                                .build());
            }
        } catch (Exception e) {
            log.info("Clean bucket fail");
            e.printStackTrace();
        }
    }

    @Test
    public void storeImageTest(){
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var multipartFile = ImageHelper.multipartFile1();

        registerNewUser.register(fullName,credentials);

        var user = authenticatedUserRepository.findByEmail(credentials.email());
        // When
        var imageId = imageService.storeImage(user.userId(), ImageHelper.multipartFile1());

        // Then
        var image = imageRepository.findImageByImageId(imageId);
        assertNotNull(image);
        assertEquals(user.userId(), image.userId());
        assertEquals(multipartFile.getSize(), image.metaInfo().imageSize());
        assertEquals(multipartFile.getOriginalFilename(),
                image.metaInfo().imageNameWithExtension().toString());
        assertEquals(".jpg", image.metaInfo().imageNameWithExtension().imageExtension().toString());

        var objectsInMinio = minioClient.listObjects(ListObjectsArgs.builder().bucket("files").build());
        AtomicInteger countObjectsInMinio = new AtomicInteger();
        objectsInMinio.forEach( it -> countObjectsInMinio.addAndGet(1));
        assertEquals(1, countObjectsInMinio.get());
    }

    @Test
    public void storeImageWithIllegalExtensionTest(){
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var multipartFile = ImageHelper.multipartFile1();

        registerNewUser.register(fullName,credentials);

        var user = authenticatedUserRepository.findByEmail(credentials.email());
        // When
        var ex = assertThrows(IllegalArgumentException.class, () -> imageService.storeImage(user.userId(), ImageHelper.multipartFileWithIllegalException()));

        // Then
        assertEquals(ex.getMessage(), "Invalid extension: " + ImageHelper.illegalExtension);
    }

    @Test
    public void getPartImagesOfUserTest(){
        //Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var multipartFile = ImageHelper.multipartFile1();

        registerNewUser.register(fullName,credentials);

        var user = authenticatedUserRepository.findByEmail(credentials.email());

        var imageId1 = imageService.storeImage(user.userId(), ImageHelper.multipartFile1());
        var imageId2 = imageService.storeImage(user.userId(), ImageHelper.multipartFile1());

        // When

        var images = getPartImagesOfUser.get(user.userId(), new PartOfResources(1L,1L));

        // Then

        assertEquals(images.size(), 1);
    }

    @Test
    public void deleteImageByImageIdTest(){
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var multipartFile = ImageHelper.multipartFile1();

        registerNewUser.register(fullName,credentials);

        var user = authenticatedUserRepository.findByEmail(credentials.email());

        var imageId1 = imageService.storeImage(user.userId(), ImageHelper.multipartFile1());
        var imageId2 = imageService.storeImage(user.userId(), ImageHelper.multipartFile1());

        // When
        imageService.deleteImageByImageId(user.userId(), imageId1);

        // Then
        var images = imageRepository.findImagesByUserId(user.userId(), new PartOfResources(0L, 2L));
        assertEquals(images.size(), 1);
        assertEquals(images.get(0).imageId(), imageId2);
    }

    @Test
    public void deleteImageByImageIdAnotherUser(){
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var multipartFile = ImageHelper.multipartFile1();
        var userId = UserHelper.UserId();

        registerNewUser.register(fullName,credentials);

        var user = authenticatedUserRepository.findByEmail(credentials.email());

        var imageId1 = imageService.storeImage(user.userId(), multipartFile);

        // When
        var ex = assertThrows(ResourceOwnershipException.class, () -> imageService.deleteImageByImageId(userId, imageId1));
        assertEquals("Image with id " + imageId1.value().toString()
                + " does not belong to user with id " + userId.value().toString(), ex.getMessage());
    }

    @Test
    public void downloadImageTest() throws IOException {
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var multipartFile = ImageHelper.multipartFile1();
        var userId = UserHelper.UserId();

        registerNewUser.register(fullName,credentials);

        var user = authenticatedUserRepository.findByEmail(credentials.email());

        var imageId1 = imageService.storeImage(user.userId(), multipartFile);

        // When
        var image = imageService.downloadImage(imageId1, user.userId());

        // Then
        assertArrayEquals(multipartFile.getBytes(), image);
    }
}
