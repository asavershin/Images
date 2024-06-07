package com.github.asavershin.api.application.in.services.user;

import com.github.asavershin.api.domain.user.Credentials;

public interface JwtService {

    /**
     * Generates an access token for the provided credentials.
     *
     * @param credentials The credentials used to generate the access token.
     * @return A string representing the generated access token.
     */
    String generateAccessToken(Credentials credentials);

    /**
     * Generates a refresh token for the provided credentials.
     *
     * @param credentials The credentials used to generate the refresh token.
     * @return A string representing the generated refresh token.
     */
    String generateRefreshToken(Credentials credentials);

    /**
     * Extracts the subject from the provided JWT.
     *
     * @param jwt The JWT from which the subject should be extracted.
     * @return A string representing the extracted subject.
     */
    String extractSub(String jwt);
}
