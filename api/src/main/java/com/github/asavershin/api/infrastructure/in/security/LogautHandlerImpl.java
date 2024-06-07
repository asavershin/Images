package com.github.asavershin.api.infrastructure.in.security;

import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.application.in.services.user.JwtService;
import com.github.asavershin.api.config.properties.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogautHandlerImpl implements LogoutHandler {
    /**
     * TokenRepository dependency.
     */
    private final TokenRepository tokenRepository;
    /**
     * JwtService dependency.
     */
    private final JwtService jwtService;

    /**
     * Not final to allow spring using proxy.
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the current principal details
     */
    @Override
    public void logout(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(JwtProperties.START_OF_TOKEN);
        tokenRepository.deleteAllTokensByUserEmail(jwtService.extractSub(jwt));
    }
}
