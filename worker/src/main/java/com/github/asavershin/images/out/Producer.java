package com.github.asavershin.images.out;

import com.github.asavershin.images.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.github.asavershin.images.config.KafkaConf.ALL_ACKS_KAFKA_TEMPLATE;

@Component
public class Producer {
    private final KafkaTemplate<String, Task> producer;

    public Producer(
            final @Qualifier(ALL_ACKS_KAFKA_TEMPLATE)
            KafkaTemplate<String, Task> allAcksKafkaTemplate
    ) {
        producer = allAcksKafkaTemplate;
    }

    @Transactional
    public void produce(final Task event, final String topic) {
        producer.send(
                        topic,
                        event
                );
    }
}
