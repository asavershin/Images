package com.github.asavershin.api.domaintest;

import com.github.asavershin.api.common.ImageHelper;
import com.github.asavershin.api.common.UserHelper;
import com.github.asavershin.api.domain.ResourceOwnershipException;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.MetaInfo;
import com.github.asavershin.api.domain.user.UserId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImageTest {

    @Test
    void testAddNewImage() {
        //Given
        ImageId imageId = ImageHelper.imageId();
        MetaInfo metaInfo = ImageHelper.metaInfo1();
        UserId userId = UserHelper.UserId();

        //When
        Image image = new Image(imageId, metaInfo, userId);

        //Then
        assertEquals(image.imageId(), imageId);
        assertEquals(image.metaInfo(), metaInfo);
        assertEquals(image.userId(), userId);
    }

    @Test
    void testFoundedImage() {
        //Given
        ImageId imageId = ImageHelper.imageId();
        MetaInfo metaInfo = ImageHelper.metaInfo1();
        UserId userId = UserHelper.UserId();
        // When
        Image image = new Image(imageId, metaInfo, userId);
        // Then
        assertEquals(image.imageId(), imageId);
        assertEquals(image.metaInfo(), metaInfo);
        assertEquals(image.userId(), userId);
    }

    @Test
    void testBelongsToUser() {
        // Given
        ImageId imageId = ImageHelper.imageId();
        MetaInfo metaInfo = ImageHelper.metaInfo1();
        UserId userId = UserHelper.UserId();
        UserId otherUserId = UserHelper.UserId();

        // When
        Image image = new Image(imageId, metaInfo, userId);
        assertDoesNotThrow(() -> image.belongsToUser(userId));
        var exception = assertThrows(ResourceOwnershipException.class, () -> image.belongsToUser(otherUserId));

        // Then
        assertDoesNotThrow(() -> image.belongsToUser(userId));
        assertEquals("Image with id " + imageId.value().toString() +
                " does not belong to user with id " + otherUserId.value().toString(), exception.getMessage());
    }

    @Test
    void testEquals() {
        var imageId = ImageId.nextIdentity();
        var userId = UserId.nextIdentity();

        MetaInfo metaInfo1 = ImageHelper.metaInfo1();
        Image image1 = new Image(imageId, metaInfo1, userId);

        MetaInfo metaInfo2 = ImageHelper.metaInfo1();
        Image image2 = new Image(imageId, metaInfo2, userId);

        assertTrue(image1.equals(image2));
        assertTrue(image2.equals(image1));

        assertTrue(image1.equals(image1));

        assertFalse(image1.equals(null));

        ImageId imageId3 = ImageId.nextIdentity();
        MetaInfo metaInfo3 = ImageHelper.metaInfo3();
        UserId userId3 = UserHelper.UserId();
        Image image3 = new Image(imageId3, metaInfo3, userId3);

        assertFalse(image1.equals(image3));
        assertFalse(image3.equals(image1));
    }

}


