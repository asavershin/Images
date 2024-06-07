package com.github.asavershin.api.application.out;

public interface TokenRepository {
    /**
     * Retrieves the access token for the given email.
     *
     * @param email the email of the user
     * @return the access token for the given email
     */
    String getAccessToken(String email);

    /**
     * Retrieves the refresh token for the given email.
     *
     * @param email the email of the user
     * @return the refresh token for the given email
     */
    String getRefreshToken(String email);

    /**
     * Saves the refresh token for the given user with
     * the specified JWT token and expiration time.
     *
     * @param username   the username of the user
     * @param jwtToken   the JWT token to be saved
     * @param expiration the expiration time of the token
     */
    void saveRefreshToken(String username, String jwtToken, Long expiration);

    /**
     * Saves the access token for the given user with
     * the specified JWT token and expiration time.
     *
     * @param username   the username of the user
     * @param jwtToken   the JWT token to be saved
     * @param expiration the expiration time of the token
     */
    void saveAccessToken(String username, String jwtToken, Long expiration);

    /**
     * Deletes all tokens associated with the given user email.
     *
     * @param username the username of the user
     */
    void deleteAllTokensByUserEmail(String username);
}
