package com.github.asavershin.worker.integrations;

import com.github.asavershin.worker.config.CacheRedisConfig;
import com.github.asavershin.worker.config.MinioConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@EmbeddedKafka
@ContextConfiguration(initializers = {MinioConfig.Initializer.class, CacheRedisConfig.Initializer.class})
@ActiveProfiles("blackwhite")
public abstract class BlackWhiteTest {
}
