package com.github.asavershin.api.integrations;

import com.github.asavershin.api.config.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@EmbeddedKafka
@ContextConfiguration(initializers = {PostgreConfig.Initializer.class, MinioConfig.Initializer.class, CacheRedisConfig.Initializer.class})
public abstract class AbstractTest {
}
