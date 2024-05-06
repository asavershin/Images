package com.github.asavershin.images.in;

import com.github.asavershin.images.Task;
import com.github.asavershin.images.WorkerManager;
import com.github.asavershin.images.out.Producer;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    private final WorkerManager manager;
    private final Producer producer;

    @Value("${app.wiptopic}")
    @NotNull(message = "Topic for KafkaProducer is null")
    private String wip;
    @Value("${app.donetopic}")
    @NotNull(message = "Topic for KafkaProducer is null")
    private String done;

    @KafkaListener(
            topics = "${app.wiptopic}",
            groupId = "${app.group-id}",
            concurrency = "${app.replicas}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(
            final ConsumerRecord<String, Task> record,
            final Acknowledgment acknowledgment
    ) {
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        var task = manager.start(record.value());
        if (task.getFilters().isEmpty()) {
            producer.produce(task, done);
        } else {
            producer.produce(task, wip);
        }
        acknowledgment.acknowledge();
    }
}
