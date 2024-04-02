package com.github.asavershin.api.application.in.services.user.impl;

import com.github.asavershin.api.application.in.services.user.JwtService;
import com.github.asavershin.api.config.properties.JwtProperties;
import com.github.asavershin.api.domain.user.AuthenticatedUser;
import com.github.asavershin.api.domain.user.Credentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceIml implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String generateAccessToken(Credentials credentials) {
        return buildToken(credentials, jwtProperties.getAccessExpiration());
    }

    @Override
    public String generateRefreshToken(Credentials credentials) {
        return buildToken(credentials, jwtProperties.getRefreshExpiration());
    }

    @Override
    public String extractSub(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    private String buildToken(Credentials credentials, long expiration) {
        return Jwts
                .builder()
                .setSubject(credentials.email())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
