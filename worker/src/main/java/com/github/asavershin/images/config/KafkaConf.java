package com.github.asavershin.images.config;

import com.github.asavershin.images.Task;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaConf {
    /**
     * Static final string constant for the Kafka template bean name.
     */
    public static final String ALL_ACKS_KAFKA_TEMPLATE = "allAcksKafkaTemplate";
    /**
     * Injected producer properties.
     */
    @NotNull(message = "KafkaConf is invalid: prodProp is null")
    private final ProducerProp prodProp;
    /**
     * Injected Kafka properties.
     */
    @NotNull(message = "KafkaConf is invalid: KafkaProperties is null")
    private final KafkaProperties properties;
    /**
     * Creates a new Kafka topic.
     *
     * @param topic      the name of the topic
     * @param partitions  the number of partitions
     * @param replicas    the number of replicas
     * @return a new Kafka topic
     */
    @Bean
    public NewTopic wipTopic(
            final @Value("${app.wiptopic}") String topic,
            final @Value("${app.partitions}") int partitions,
            final @Value("${app.replicas}") short replicas) {

        return new NewTopic(topic, partitions, replicas);
    }
    /**
     * Creates a new Kafka topic with the specified name,
     * number of partitions, and replicas.
     *
     * @param topic      the name of the topic
     * @param partitions the number of partitions for the topic
     * @param replicas   the number of replicas for the topic
     * @return a new Kafka topic with the specified configuration
     */
    @Bean
    public NewTopic doneTopic(
            final @Value("${app.donetopic}") String topic,
            final @Value("${app.partitions}") int partitions,
            final @Value("${app.replicas}") short replicas) {

        return new NewTopic(topic, partitions, replicas);
    }
    /**
     * Creates a new Kafka template with all acknowledgments.
     *
     * @return a new Kafka template with all acknowledgments
     */
    @Bean(ALL_ACKS_KAFKA_TEMPLATE)
    public KafkaTemplate<String, Task> allAcksKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                    props.put(ProducerConfig.RETRIES_CONFIG,
                            prodProp.getRetries());
                })
        );
    }

    private <T> ProducerFactory<String, T> producerFactory(
            final Consumer<Map<String, Object>> enchanter
    ) {
        var props = properties.buildProducerProperties(null);
        // Работаем со строками
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        // Партиция одна, так что все равно как роутить
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                RoundRobinPartitioner.class);
        // Отправляем сообщения сразу
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        // До-обогащаем конфигурацию
        enchanter.accept(props);
        return new DefaultKafkaProducerFactory<>(props);
    }
    /**
     * Creates a Kafka listener container factory.
     *
     * @return a new Kafka listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Task>
    kafkaListenerContainerFactory() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, Task>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Task.class)
                )
        );
        factory.getContainerProperties().setAckMode(
                ContainerProperties.AckMode.MANUAL
        );
        return factory;
    }


}

