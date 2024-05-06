package com.github.asavershin.images.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for setting up the RedisTemplate.
 *
 * @author Asavershin
 */
@Configuration
public class RedisConf {

    /**
     * Creates a new instance of {@link RedisTemplate} with the provided
     * {@link RedisConnectionFactory}.
     *
     * @param connectionFactory the RedisConnectionFactory to use
     * @return a new instance of {@link RedisTemplate}
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            final RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(
                new GenericToStringSerializer<>(Object.class)
        );
        return template;
    }
}
