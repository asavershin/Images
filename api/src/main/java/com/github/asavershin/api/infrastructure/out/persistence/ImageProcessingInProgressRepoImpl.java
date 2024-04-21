package com.github.asavershin.api.infrastructure.out.persistence;

import com.github.asavershin.api.domain.filter.RequestId;
import com.github.asavershin.api.domain.filter.ImageProcessingInProgress;
import com.github.asavershin.api.domain.filter.ImageProcessingInProgressRepo;
import com.github.asavershin.api.domain.filter.Status;
import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;

import static asavershin.generated.package_.Tables.IMAGE;
import static asavershin.generated.package_.Tables.USER_IMAGES;
import static asavershin.generated.package_.tables.ImageProcessingEvent.IMAGE_PROCESSING_EVENT;

@Repository
@RequiredArgsConstructor
public class ImageProcessingInProgressRepoImpl
        implements ImageProcessingInProgressRepo,
        RecordMapper<Record, ImageProcessingInProgress> {
    /**
     * The Jooq DSL context for database operations.
     */
    private final DSLContext dsl;

    /**
     * Saves an {@link ImageProcessingInProgress} event to the database.
     *
     * @param event The {@link ImageProcessingInProgress} event to be saved.
     */
    @Override
    public void save(final ImageProcessingInProgress event) {
        var count = dsl.update(IMAGE_PROCESSING_EVENT)
                .set(IMAGE_PROCESSING_EVENT.PROCESSED_IMAGE_ID,
                        event.result().imageId().value()
                )
                .set(IMAGE_PROCESSING_EVENT.STATUS_NAME,
                        event.result().status().toString()
                )
                .where(IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID
                        .eq(event.requestId().value())
                )
                .execute();
        if (count == 0) {
            throw new RuntimeException("No rows were updated."
                    + " The object does not exist in the database.");
        }
    }

    /**
     * Retrieves an {@link ImageProcessingInProgress}
     * event from the database by its IDs.
     *
     * @param requestId The ID of the {@link ImageProcessingInProgress}
     *                  event to be retrieved.
     * @param imageId   The ID of the original image
     * @param userId    The ID of the user who owns the image
     * @return The {@link ImageProcessingInProgress}
     * event with the given ID, or null if not found.
     */
    @Override
    public ImageProcessingInProgress findByRequestIdImageIdUserId(
            final RequestId requestId,
            final ImageId imageId,
            final UserId userId
    ) {
        return dsl.select(IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID,
                        IMAGE_PROCESSING_EVENT.PROCESSED_IMAGE_ID,
                        IMAGE_PROCESSING_EVENT.STATUS_NAME,
                        IMAGE_PROCESSING_EVENT.ORIGINAL_IMAGE_ID)
                .from(IMAGE_PROCESSING_EVENT)
                .join(IMAGE).on(IMAGE.IMAGE_ID.eq(imageId.value()))
                .join(USER_IMAGES).on(USER_IMAGES.IMAGE_ID.eq(imageId.value()))
                .where(
                        IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID
                                .eq(requestId.value())
                                .and(USER_IMAGES.USER_ID.eq(userId.value()))
                )
                .fetchOne(this);
    }

    /**
     * Maps a database record to an {@link ImageProcessingInProgress} object.
     *
     * @param record The database record to be mapped.
     * @return The {@link ImageProcessingInProgress}
     * object corresponding to the given record.
     */
    @Override
    public @Nullable ImageProcessingInProgress map(final Record record) {
        var uuid = record.get(
                IMAGE_PROCESSING_EVENT.PROCESSED_IMAGE_ID
        );
        var processedImageId = uuid == null ? null : new ImageId(uuid);
        return new ImageProcessingInProgress(
                new RequestId(record.get(
                        IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID
                )),
                processedImageId,
                Status.fromString(record.get(
                        IMAGE_PROCESSING_EVENT.STATUS_NAME
                )),
                new ImageId(record.get(
                        IMAGE_PROCESSING_EVENT.ORIGINAL_IMAGE_ID
                ))
        );
    }
}
