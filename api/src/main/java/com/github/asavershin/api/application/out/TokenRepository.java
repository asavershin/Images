package com.github.asavershin.api.application.out;

public interface TokenRepository {
    String getAccessToken(String email);

    String getRefreshToken(String email);

    void saveRefreshToken(String username, String jwtToken, Long expiration);

    void saveAccessToken(String username, String jwtToken, Long expiration);

    void deleteAllTokensByUserEmail(String username);
}
