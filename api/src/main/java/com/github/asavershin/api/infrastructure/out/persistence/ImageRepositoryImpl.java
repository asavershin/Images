package com.github.asavershin.api.infrastructure.out.persistence;

import com.github.asavershin.api.domain.PartOfResources;
import com.github.asavershin.api.domain.image.Image;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.ImageNameWithExtension;
import com.github.asavershin.api.domain.image.ImageRepository;
import com.github.asavershin.api.domain.image.MetaData;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static asavershin.generated.package_.Tables.USER_IMAGES;
import static asavershin.generated.package_.Tables.IMAGE;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl
        implements ImageRepository, RecordMapper<Record, Image> {
    /**
     * The DSLContext object is used to interact with the database.
     */
    private final DSLContext dslContext;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void save(final Image image) {
        dslContext.insertInto(IMAGE)
                .set(IMAGE.IMAGE_ID, image.imageId().value())
                .set(
                        IMAGE.IMAGE_NAME,
                        image.metaInfo().imageNameWithExtension().imageName()
                )
                .set(IMAGE.IMAGE_SIZE, image.metaInfo().imageSize())
                .set(IMAGE.IMAGE_EXTENSION,
                        image.metaInfo().imageNameWithExtension()
                                .imageExtension().toString())
                .execute();
        dslContext.insertInto(USER_IMAGES)
                .set(USER_IMAGES.IMAGE_ID, image.imageId().value())
                .set(USER_IMAGES.USER_ID, image.userId().value())
                .execute();
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public List<Image> findImagesByUserId(final UserId userId,
                                          final PartOfResources page) {
        return dslContext.select(IMAGE.fields()).select(USER_IMAGES.USER_ID)
                .from(IMAGE)
                .join(USER_IMAGES).using(IMAGE.IMAGE_ID)
                .offset(page.pageNumber() * page.pageSize())
                .limit(page.pageSize())
                .fetch(this);
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public Image findImageByImageId(final ImageId imageId) {
        return dslContext.select(IMAGE.fields()).select(USER_IMAGES.USER_ID)
                .from(IMAGE)
                .join(USER_IMAGES).using(IMAGE.IMAGE_ID)
                .where(IMAGE.IMAGE_ID.eq(imageId.value()))
                .fetchOne(this);
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void deleteImageByImageId(final Image imageId) {
        dslContext.deleteFrom(IMAGE)
                .where(IMAGE.IMAGE_ID.eq(imageId.imageId().value()))
                .execute();
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public Image map(final Record record) {
        var imageId = record.get(IMAGE.IMAGE_ID);
        var imageName = record.get(IMAGE.IMAGE_NAME);
        var userId = record.get(USER_IMAGES.USER_ID);
        var imageSize = record.get(IMAGE.IMAGE_SIZE);
        var imageExtension = record.get(IMAGE.IMAGE_EXTENSION);
        return new Image(
                new ImageId(imageId),
                new MetaData(
                        ImageNameWithExtension.founded(
                                imageName,
                                imageExtension
                        ),
                        imageSize
                ),
                new UserId(userId)
        );
    }
}

