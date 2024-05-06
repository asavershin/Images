package com.github.asavershin.worker.in;

import com.github.asavershin.worker.InvalidWorker;
import com.github.asavershin.worker.dto.DoneTask;
import com.github.asavershin.worker.dto.Task;
import com.github.asavershin.worker.services.WorkerManager;
import com.github.asavershin.worker.out.CacheRepository;
import com.github.asavershin.worker.out.producers.DoneProducer;
import com.github.asavershin.worker.out.producers.WipProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    private final WorkerManager manager;
    private final WipProducer wipProducer;
    private final DoneProducer doneProducer;
    private final CacheRepository cacheRepository;

    @KafkaListener(
            topics = "${app.wiptopic}",
            groupId = "${app.group-id}",
            concurrency = "${app.replicas}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(
            final @Payload Task record,
            final Acknowledgment acknowledgment
    ) {
        log.info(
                "Received message: {}",
                record.toString()
        );
        Task task;
        try {
            task = manager.start(record);
        } catch (final InvalidWorker e) {
            log.info(e.getMessage());
            return;
        }
        if (task == null) {
            acknowledgment.acknowledge();
            return;
        }
        if (task.getFilters().isEmpty()) {
            doneProducer.produce(new DoneTask(task.getImageId(), task.getRequestId()));
        } else {
            wipProducer.produce(task);
        }
        acknowledgment.acknowledge();
    }
}
