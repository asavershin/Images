package com.github.asavershin.worker.out;

import com.github.asavershin.worker.Task;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.github.asavershin.worker.config.KafkaConf.WIP_PRODUCER;

@Component
public class WipProducer {
    private final KafkaTemplate<String, Task> producer;
    @Value("${app.wiptopic}")
    @NotNull(message = "Topic for KafkaProducer is null")
    private String topic;
    public WipProducer(
            final @Qualifier(WIP_PRODUCER)
            KafkaTemplate<String, Task> allAcksKafkaTemplate
    ) {
        producer = allAcksKafkaTemplate;
    }

    @Transactional
    public void produce(final Task event) {
        producer.send(
                        topic,
                        event
                );
    }
}
