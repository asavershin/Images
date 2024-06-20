package com.github.asavershin.worker.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ClientSideConfig;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableRetry
@Profile("imagga")
public class ImaggaConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisClient redisClient(final RedisProperties redisProperties) {
        return RedisClient.create(
                RedisURI.Builder.redis(redisProperties.getHost(), redisProperties.getPort())
                        .withPassword(redisProperties.getPassword().toCharArray()).build());
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager(final RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> redisConnection = redisClient
                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager
                .builderFor(redisConnection)
                .build();
    }

    @Bean
    public Bucket bucketConfiguration(final ProxyManager<String> proxyManager) {
        return proxyManager.builder().build(
                "imagga",
                () -> BucketConfiguration.builder()
                        .addLimit(
                                Bandwidth.builder()
                                        .capacity(33)
                                        .refillIntervally(
                                                33,
                                                Duration.ofDays(1L)
                                        )
                                        .build()
                        )
                        .build()
        );
    }
}
