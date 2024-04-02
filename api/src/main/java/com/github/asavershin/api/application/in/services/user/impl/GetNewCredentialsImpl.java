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
    private final TokenRepository tokenRepository;
    private final TryToLogin tryToLogin;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    public ApplicationCredentials get(Credentials credentials) {
        var authenticatedUser = tryToLogin.login(credentials);
        var accessToken = jwtService.generateAccessToken(authenticatedUser.userCredentials());
        var refreshToken = jwtService.generateRefreshToken(authenticatedUser.userCredentials());
        var email = authenticatedUser.userCredentials().email();
        tokenRepository.deleteAllTokensByUserEmail(email);
        tokenRepository.saveRefreshToken(email, refreshToken, jwtProperties.getRefreshExpiration());
        tokenRepository.saveAccessToken(email, accessToken, jwtProperties.getAccessExpiration());
        return new ApplicationCredentials(accessToken, refreshToken);
    }
}
