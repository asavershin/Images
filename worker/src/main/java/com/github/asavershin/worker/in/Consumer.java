//package com.github.asavershin.images.in;
//
//import com.github.asavershin.images.DoneTask;
//import com.github.asavershin.images.Task;
//import com.github.asavershin.images.WorkerManager;
//import com.github.asavershin.images.out.CacheRepository;
//import com.github.asavershin.images.out.DoneProducer;
//import com.github.asavershin.images.out.WipProducer;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class Consumer {
//    private final WorkerManager manager;
//    private final WipProducer wipProducer;
//    private final DoneProducer doneProducer;
//    private final CacheRepository cacheRepository;
//
//    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//    @KafkaListener(
//            topics = "${app.wiptopic}",
//            groupId = "${app.group-id}",
//            concurrency = "${app.replicas}",
//            containerFactory = "kafkaListenerContainerFactory"
//    )
//    @Transactional
//    public void consume(
//            final @Payload List<Task> records
//    ) {
//        log.info("Consumed: {}", records.size());
//        for (var record : records) {
//            executorService.execute(() -> processRecord(record));
//        }
//    }
//
//    private void processRecord(
//            final Task record
//    ) {
//        try {
//            log.info("Received message: {}", record.toString());
//            var task = manager.start(record);
//            if (task == null) {
//                return;
//            }
//            if (task.getFilters().isEmpty()) {
//                doneProducer.produce(new DoneTask(task.getImageId(), task.getRequestId()));
//            } else {
//                wipProducer.produce(task);
//            }
//        } catch (Exception ex) {
//            log.info(ex.getMessage());
//            cacheRepository.deleteCache(
//                    record.getRequestId() + record.getImageId()
//            );
//            wipProducer.produce(record);
//        }
//    }
//}
//
////{
////        "imageId": "example",
////        "requestId": "987654",
////        "filters": [
////        "ROTATE"
////        ]
////}
