package com.github.asavershin.api.integrations;


import com.github.asavershin.api.application.in.services.image.ImageService;
import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.common.ImageHelper;
import com.github.asavershin.api.common.UserHelper;
import com.github.asavershin.api.config.properties.MinIOProperties;
import com.github.asavershin.api.domain.filter.GetStatusEvent;
import com.github.asavershin.api.domain.filter.StoreFinishedIPEInfo;
import com.github.asavershin.api.domain.filter.StoreStartedIPEInfo;
import com.github.asavershin.api.domain.user.AuthenticatedUserRepository;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static asavershin.generated.package_.Tables.IMAGE;
import static asavershin.generated.package_.Tables.IMAGE_PROCESSING_EVENT_FILTER;
import static asavershin.generated.package_.Tables.USERS;
import static asavershin.generated.package_.tables.ImageProcessingEvent.IMAGE_PROCESSING_EVENT;
import static com.github.asavershin.api.common.EventHelper.getEventFromDB;
import static com.github.asavershin.api.common.EventHelper.started;
import static com.github.asavershin.api.common.UserHelper.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class EventLogicTest
        extends AbstractTest {
    @Autowired
    private DSLContext dsl;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private RegisterNewUser newUser;
    @Autowired
    private AuthenticatedUserRepository authRepo;
    @Autowired
    private ImageService imageService;
    @Autowired
    private StoreStartedIPEInfo startedEvent;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOProperties minioProperties;
    @Autowired
    private GetStatusEvent statusEvent;
    @Autowired
    private StoreFinishedIPEInfo finishedEvent;

    @BeforeEach
    public void clearDB() {
        dsl.delete(USERS).execute();
        dsl.delete(IMAGE).execute();
        dsl.delete(IMAGE_PROCESSING_EVENT).execute();
        dsl.delete(IMAGE_PROCESSING_EVENT_FILTER).execute();
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
    public void testStartedEvent() {
        // Given
        var testUser = user1();
        newUser.register(testUser);
        var imageId = imageService.storeImage(testUser.userId(), ImageHelper.multipartFile1());
        var newEvent = started(imageId);
        // When
        startedEvent.store(newEvent, testUser.userId());
        // Then
        var storedEvent = getEventFromDB(dsl, newEvent.requestId());
        assertEquals(storedEvent.size(), 3);
        assertNotNull(storedEvent);
        assertNotNull(storedEvent.get(0).getValue(IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID));
        assertEquals(storedEvent.get(0).getValue(IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID),
                newEvent.requestId().value());
        for (int i = 1; i < newEvent.filters().size(); ++i) {
            assertEquals(storedEvent.get(i).getValue(IMAGE_PROCESSING_EVENT_FILTER.IMAGE_FILTER_NAME),
                    newEvent.filters().get(i).toString());
        }
    }
    @Test
    public void testInProgress() {
        // Given
        var testUser = user1();
        newUser.register(testUser);
        var imageId = imageService.storeImage(testUser.userId(), ImageHelper.multipartFile1());
        var newEvent = started(imageId);
        startedEvent.store(newEvent, testUser.userId());
        // When
        var se = statusEvent.get(newEvent.requestId(), testUser.userId(), imageId);
        // Then
        assertNotNull(se);
        assertEquals(se.result().status().toString(), "WIP");
        assertEquals(imageId, se.result().imageId());
        assertEquals(se.requestId(), newEvent.requestId());
    }
    @Test
    public void testFinishedEvent() {
        // Given
        var testUser = user1();
        newUser.register(testUser);
        var imageId = imageService.storeImage(testUser.userId(), ImageHelper.multipartFile1());
        var newEvent = started(imageId);
        startedEvent.store(newEvent, testUser.userId());
        var processedId = imageService.storeImage(testUser.userId(), ImageHelper.multipartFile1());;
        // When
        finishedEvent.store(newEvent.requestId().value().toString(), processedId.value().toString());
        // Then
        var se = statusEvent.get(newEvent.requestId(), testUser.userId(), imageId);
        assertNotNull(se);
        assertEquals(se.result().status().toString(), "DONE");
        assertNotNull(se.result().imageId());
        assertEquals(se.result().imageId(), processedId);
        assertEquals(se.requestId(), newEvent.requestId());
    }
}
