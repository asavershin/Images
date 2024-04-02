package com.github.asavershin.api.application.in.services.user;

import com.github.asavershin.api.domain.user.Credentials;

public interface JwtService {

    String generateAccessToken(Credentials credentials);

    String generateRefreshToken(
            Credentials credentials
    );

    String extractSub(String jwt);
}
