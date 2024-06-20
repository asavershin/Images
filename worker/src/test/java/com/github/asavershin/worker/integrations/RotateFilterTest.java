package com.github.asavershin.worker.integrations;

import com.github.asavershin.worker.config.MinIOProperties;
import com.github.asavershin.worker.services.Worker;
import com.github.asavershin.worker.out.MinioService;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.MinioProperties;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.asavershin.worker.ImageHelper.createTestImage;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
public class RotateFilterTest extends RotateTest{
    @Autowired
    private MinioService minioService;
    @Autowired
    private Worker worker;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOProperties minioProperties;

    @BeforeEach
    public void clearDB() {
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
    public void rotateWorkerTest() {
        minioService.saveFile(createTestImage(100, 200), "example");
        worker.doWork("example", true);

        var objectsInMinio = minioClient.listObjects(ListObjectsArgs.builder().bucket("files").build());
        AtomicInteger countObjectsInMinio = new AtomicInteger();
        objectsInMinio.forEach( it -> countObjectsInMinio.addAndGet(1));
        assertEquals(2, countObjectsInMinio.get());
    }
}
