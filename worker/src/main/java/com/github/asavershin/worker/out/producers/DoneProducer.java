package com.github.asavershin.worker.out.producers;

import com.github.asavershin.worker.dto.DoneTask;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.github.asavershin.worker.config.KafkaConf.DONE_PRODUCER;

@Component
public class DoneProducer {
    private final KafkaTemplate<String, DoneTask> producer;
    @Value("${app.donetopic}")
    @NotNull(message = "Topic for KafkaProducer is null")
    private String topic;
    public DoneProducer(
            final @Qualifier(DONE_PRODUCER)
            KafkaTemplate<String, DoneTask> allAcksKafkaTemplate
    ) {
        producer = allAcksKafkaTemplate;
    }

    @Transactional
    public void produce(final DoneTask event) {
        producer.send(
                topic,
                event
        );
    }
}
