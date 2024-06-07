package com.github.asavershin.api.infrastructure.in.security;

import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.application.in.services.user.JwtService;
import com.github.asavershin.api.config.properties.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * JwtService dependency injection.
     *
     * @see com.github.asavershin.api.application.in.services.user.JwtService
     */
    private final JwtService jwtService;

    /**
     * UserDetailsService dependency injection.
     *
     * @see com.github.asavershin.api.application.out.TokenRepository
     */
    private final UserDetailsService userDetailsService;

    /**
     * TokenRepository dependency injection.
     *
     * @see com.github.asavershin.api.application.out.TokenRepository
     */
    private final TokenRepository tokenRepository;

    @Override
    protected final void doFilterInternal(
            final @NonNull HttpServletRequest request,
            final @NonNull HttpServletResponse response,
            final @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var path = request.getServletPath();

        if (path.contains("/register") || path.contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(JwtProperties.START_OF_TOKEN);
        var email = jwtService.extractSub(jwt);

        String token;
        var pathContainsRefreshToken = path.contains("/refresh-token");
        if (pathContainsRefreshToken) {
            token = tokenRepository.getRefreshToken(email);
        } else {
            token = tokenRepository.getAccessToken(email);
        }
        if (!token.equals(jwt)) {
            tokenRepository.deleteAllTokensByUserEmail(email);
            return;
        }

        var authToken = new UsernamePasswordAuthenticationToken(
                userDetailsService.loadUserByUsername(email),
                null,
                null
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
