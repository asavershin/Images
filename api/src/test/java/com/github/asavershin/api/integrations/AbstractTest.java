package com.github.asavershin.api.integrations;

import com.github.asavershin.api.config.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {PostgreConfig.Initializer.class, MinioConfig.Initializer.class, CacheRedisConfig.Initializer.class})
public abstract class AbstractTest {
}
