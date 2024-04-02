package com.github.asavershin.api.application.in.services.user.impl;

import com.github.asavershin.api.application.in.services.user.ApplicationCredentials;
import com.github.asavershin.api.application.in.services.user.GetNewCredentialsUsingRefreshToken;
import com.github.asavershin.api.application.in.services.user.JwtService;
import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.config.properties.JwtProperties;
import com.github.asavershin.api.domain.user.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNewCredentialsUsingRefreshTokenImpl implements GetNewCredentialsUsingRefreshToken {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    public ApplicationCredentials get(Credentials credentials) {
        tokenRepository.deleteAllTokensByUserEmail(credentials.email());
        var at = jwtService.generateAccessToken(credentials);
        var rt = jwtService.generateRefreshToken(credentials);
        tokenRepository.saveAccessToken(credentials.email(), at, jwtProperties.getAccessExpiration());
        tokenRepository.saveRefreshToken(credentials.email(), rt, jwtProperties.getRefreshExpiration());
        return new ApplicationCredentials(at, rt);
    }
}
