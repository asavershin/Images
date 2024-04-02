package com.github.asavershin.api.application.out;

public interface CacheRepository {
    void addCache(String key, String token, long expiration);

    String getCache(String key);

    void deleteCache(String key);
}
