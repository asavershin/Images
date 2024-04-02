package com.github.asavershin.api.infrastructure.out.persistence;

import com.github.asavershin.api.application.out.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class CacheRepositoryIml implements CacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void addCache(String key, String token, long expiration) {
        redisTemplate.opsForValue().set(key, token, expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getCache(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }
}
