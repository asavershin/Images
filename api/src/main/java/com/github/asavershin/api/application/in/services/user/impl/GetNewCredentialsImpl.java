package com.github.asavershin.api.application.in.services.user.impl;

import com.github.asavershin.api.application.in.services.user.ApplicationCredentials;
import com.github.asavershin.api.application.in.services.user.GetNewCredentials;
import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.application.in.services.user.JwtService;
import com.github.asavershin.api.config.properties.JwtProperties;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.TryToLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetNewCredentialsImpl implements GetNewCredentials {
    /**
     * Dependency for get,save,delete tokens in redis.
     */
    private final TokenRepository tokenRepository;
    /**
     * Domain service that allow user try to log in.
     */
    private final TryToLogin tryToLogin;
    /**
     * Application service that allow to do some manipulations with JWT tokens.
     */
    private final JwtService jwtService;
    /**
     * Property that contains secret and access, refresh expirations.
     */
    private final JwtProperties jwtProperties;

    @Override
    public final ApplicationCredentials get(final Credentials credentials) {
        var authenticatedUser = tryToLogin.login(credentials);
        var accessToken = jwtService
                .generateAccessToken(authenticatedUser.userCredentials());
        var refreshToken = jwtService
                .generateRefreshToken(authenticatedUser.userCredentials());
        var email = authenticatedUser.userCredentials().email();
        tokenRepository.deleteAllTokensByUserEmail(email);
        tokenRepository.saveRefreshToken(email,
                refreshToken,
                jwtProperties.getRefreshExpiration());
        tokenRepository.saveAccessToken(email,
                accessToken,
                jwtProperties.getAccessExpiration());
        return new ApplicationCredentials(accessToken, refreshToken);
    }
}
