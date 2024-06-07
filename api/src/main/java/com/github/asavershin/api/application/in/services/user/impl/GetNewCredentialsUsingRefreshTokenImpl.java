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
public class GetNewCredentialsUsingRefreshTokenImpl
        implements GetNewCredentialsUsingRefreshToken {
    /**
     * The repository for storing and retrieving tokens.
     */
    private final TokenRepository tokenRepository;

    /**
     * The service for generating and validating JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * The properties containing the configuration for JWT tokens.
     */
    private final JwtProperties jwtProperties;
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public ApplicationCredentials get(final Credentials credentials) {
        tokenRepository.deleteAllTokensByUserEmail(credentials.email());
        var at = jwtService.generateAccessToken(credentials);
        var rt = jwtService.generateRefreshToken(credentials);
        tokenRepository.saveAccessToken(
                credentials.email(),
                at,
                jwtProperties.getAccessExpiration()
        );
        tokenRepository.saveRefreshToken(
                credentials.email(),
                rt,
                jwtProperties.getRefreshExpiration()
        );
        return new ApplicationCredentials(at, rt);
    }
}
