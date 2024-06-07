package com.github.asavershin.api.infrastructure.out.storage;

import com.github.asavershin.api.application.out.CacheRepository;
import com.github.asavershin.api.application.out.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryIml implements TokenRepository {
    /**
     * The prefix for access tokens in the cache.
     */
    private final String accessKey = "ACCESS_TOKEN_";

    /**
     * The prefix for refresh tokens in the cache.
     */
    private final String refreshKey = "REFRESH_TOKEN_";

    /**
     * CacheRepository instance for storing and retrieving tokens.
     */
    private final CacheRepository cacheRepository;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public String getAccessToken(final String email) {
        return cacheRepository.getCache(accessKey + email);
    }

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public String getRefreshToken(final String email) {
        return cacheRepository.getCache(refreshKey + email);
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void saveRefreshToken(final String username,
                                 final String jwtToken,
                                 final Long expiration) {
        cacheRepository.addCache(refreshKey + username, jwtToken, expiration);
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void saveAccessToken(final String username,
                                final String jwtToken,
                                final Long expiration) {
        cacheRepository.addCache(accessKey + username, jwtToken, expiration);
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void deleteAllTokensByUserEmail(final String username) {
        cacheRepository.deleteCache(refreshKey + username);
        cacheRepository.deleteCache(accessKey + username);
    }
}
