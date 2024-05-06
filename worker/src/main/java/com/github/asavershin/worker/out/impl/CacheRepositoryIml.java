package com.github.asavershin.worker.out;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class CacheRepositoryIml implements CacheRepository {
    /**
     * Redis template is used to interact with redis server.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void addCache(final String key,
                         final String value,
                         final long expiration) {
        redisTemplate.opsForValue().set(
                key, value, expiration, TimeUnit.MILLISECONDS
        );
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public String getCache(final String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void deleteCache(final String key) {
        redisTemplate.delete(key);
    }
}
