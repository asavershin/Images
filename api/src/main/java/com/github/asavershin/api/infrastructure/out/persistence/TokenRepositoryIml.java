package com.github.asavershin.api.infrastructure.out.persistence;

import com.github.asavershin.api.application.out.CacheRepository;
import com.github.asavershin.api.application.out.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryIml implements TokenRepository {
    private final String refreshKey = "REFRESH_TOKEN_";
    private final String accessKey = "ACCESS_TOKEN_";
    private final CacheRepository cacheRepository;

    @Override
    public String getAccessToken(String email){
        return cacheRepository.getCache(accessKey + email);
    }

    @Override
    public String getRefreshToken(String email){
        return cacheRepository.getCache(refreshKey + email);
    }

    @Override
    public void saveRefreshToken(String username, String jwtToken, Long expiration) {
        cacheRepository.addCache(refreshKey + username, jwtToken, expiration);
    }

    @Override
    public void saveAccessToken(String username, String jwtToken, Long expiration) {
        cacheRepository.addCache(accessKey + username, jwtToken, expiration);
    }

    @Override
    public void deleteAllTokensByUserEmail(String username) {
        cacheRepository.deleteCache(refreshKey + username);
        cacheRepository.deleteCache(accessKey+ username);
    }
}
