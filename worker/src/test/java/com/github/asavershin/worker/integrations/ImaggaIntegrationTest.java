package com.github.asavershin.worker.integrations;

import com.github.asavershin.worker.config.MinIOProperties;
import com.github.asavershin.worker.out.MinioService;
import com.github.asavershin.worker.services.Worker;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.asavershin.worker.ImageHelper.createTestImage;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ImaggaIntegrationTest extends ImaggaTest{
    @Autowired
    private MinioService minioService;
    @Autowired
    private Worker worker;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOProperties minioProperties;
    @Autowired
    private RestTemplate restTemplate;

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
    public void testImaggaWorker() {
        minioService.saveFile(createTestImage(2000, 2000), "example");
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(MockRestRequestMatchers.requestTo("https://api.imagga.com/v2/uploads"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withSuccess("{ \"result\": { \"upload_id\": \"testUploadId\" }, \"status\": { \"text\": \"Success\", \"type\": \"success\" } }", MediaType.APPLICATION_JSON));

        // Мокируем запрос на получение тегов от Imagga
        mockServer.expect(MockRestRequestMatchers.requestTo("https://api.imagga.com/v2/tags?image_upload_id=testUploadId&limit=3"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess("{ \"result\": { \"tags\": [ { \"confidence\": 0.99, \"tag\": { \"en\": \"testTag\" } } ] }, \"status\": { \"text\": \"Success\", \"type\": \"success\" } }", MediaType.APPLICATION_JSON));
        var id = worker.doWork("example", true);

        Assertions.assertNotNull(id);
        var objectsInMinio = minioClient.listObjects(ListObjectsArgs.builder().bucket("files").build());
        AtomicInteger countObjectsInMinio = new AtomicInteger();
        objectsInMinio.forEach( it -> countObjectsInMinio.addAndGet(1));
        assertEquals(2, countObjectsInMinio.get());
    }
}
