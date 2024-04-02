package com.github.asavershin.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static java.util.Objects.isNull;

@Slf4j
public class CacheRedisConfig {

    private static volatile GenericContainer<?> redisContainer = null;

    private static GenericContainer<?> getRedisContainer() {
        var instance = redisContainer;
        if (isNull(redisContainer)) {
            synchronized (GenericContainer.class) {
                redisContainer = instance = new GenericContainer<>(
                        DockerImageName.parse("redis:7.2-rc-alpine"))
                        .withExposedPorts(6379);
                redisContainer.start();
            }
        }
        return instance;
    }

    @Component("RedisInitializer")
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            var redisContainer = getRedisContainer();
            TestPropertyValues.of(
                    "spring.data.redis.host=" + redisContainer.getHost(),
                    "spring.data.redis.port=" + redisContainer.getMappedPort(6379)
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}

