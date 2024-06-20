package com.github.asavershin.api.infrastructure.out.producers;

import com.github.asavershin.api.domain.filter.FiltersForPublisher;
import com.github.asavershin.api.domain.filter.ImageProcessingStarted;
import com.github.asavershin.api.domain.filter.StoreStartedIPEInfo;
import com.github.asavershin.api.domain.user.UserId;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.github.asavershin.api.config.KafkaConf.ALL_ACKS_KAFKA_TEMPLATE;

@Component
public class KafkaProducer {
    /**
     * KafkaTemplates for sending messages to the Kafka topic.
     */
    @NotNull(message = "KafkaTemplates for KafkaProducer is null")
    private final Map<String,
            KafkaTemplate<String, FiltersForPublisher>> templates;
    /**
     * StoreStartedIPEInfo object used to store started events.
     */
    @NotNull(message = "StartedEvent for KafkaProducer is null")
    private final StoreStartedIPEInfo startedEvent;
    /**
     * The Kafka topic to which messages will be sent.
     */
    @Value("${app.wiptopic}")
    @NotNull(message = "Topic for KafkaProducer is null")
    private String topic;

    /**
     * Constructor for KafkaProducer.
     *
     * @param allAcksKafkaTemplate the KafkaTemplate for sending messages
     * @param aStartedEvent        the StoreStartedIPEInfo object
     *                             for storing started events
     */
    public KafkaProducer(
            final @Qualifier(ALL_ACKS_KAFKA_TEMPLATE)
            KafkaTemplate<String, FiltersForPublisher> allAcksKafkaTemplate,
            final @NotNull(message =
                    "Started Event in kafka producer must not be null")
            StoreStartedIPEInfo aStartedEvent
    ) {
        templates = Map.of(
                "all", allAcksKafkaTemplate
        );
        startedEvent = aStartedEvent;
    }
    /**
     * Sends a message to the Kafka topic.
     *
     * @param event  the message to be sent
     * @param userId  the user ID associated with the message
     */
    @Transactional
    public void send(final ImageProcessingStarted event,
                     final UserId userId) {
        startedEvent.store(event, userId);
        templates.get("all")
                .send(
                        topic,
                        event.publish()
                );
    }
}
