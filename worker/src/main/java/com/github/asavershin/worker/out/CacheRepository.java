package com.github.asavershin.worker.out;

public interface CacheRepository {
    /**
     * Adds a cache entry with the specified key,
     * value, and expiration time.
     *
     * @param key     the unique identifier for the cache entry
     * @param value   the value associated with the cache entry
     * @param expiration the time in milliseconds after
     *                  which the cache entry will expire
     */
    void addCache(String key, String value, long expiration);
    /**
     * Retrieves the cache entry associated with the specified key.
     *
     * @param key the unique identifier for the cache entry
     * @return the token associated with the cache entry,
     * or null if the entry does not exist or has expired
     */
    String getCache(String key);

    void deleteCache(String key);
}
