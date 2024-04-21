package com.github.asavershin.api.infrastructure.in.consumers;

import com.github.asavershin.api.domain.filter.StoreFinishedIPEInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageDoneKafkaConsumer {
    /**
     * The service responsible for storing finished
     * image processing information in the database.
     */
    private final StoreFinishedIPEInfo storeFinishedIPEInfo;

    /**
     * Listens to the specified Kafka topic for messages
     * and processes them accordingly.
     *
     * @param record         the received Kafka message
     * @param acknowledgment the acknowledgment object
     *                       used to acknowledge the receipt of the message
     */
    @KafkaListener(
            topics = "${app.donetopic}",
            groupId = "${app.group-id}",
            concurrency = "${app.replicas}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, ImagesDoneMessage> record,
                        final Acknowledgment acknowledgment) {
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        storeFinishedIPEInfo.store(
                record.value().getRequestId(),
                record.value().getImageId()
        );
        acknowledgment.acknowledge();
    }
}
