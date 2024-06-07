package com.github.asavershin.api.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class AuthenticatedUser {
    /**
     * The unique identifier of the user.
     */
    private UserId userId;

    /**
     * The credentials of the user.
     */
    private Credentials userCredentials;

    /**
     * Constructs an instance of AuthenticatedUser
     * with the provided userId and userCredentials.
     *
     * @param aUserId the unique identifier of the user
     * @param aUserCredentials the credentials of the user
     */
    private AuthenticatedUser(final UserId aUserId,
                              final Credentials aUserCredentials) {
        setUserId(aUserId);
        setUserCredentials(aUserCredentials);
    }

    /**
     * Creates a new instance of AuthenticatedUser that becomes from repository
     * with the provided userId and userCredentials.
     *
     * @param userId the unique identifier of the user
     * @param credentials the credentials of the user
     * @return a new instance of AuthenticatedUser
     */
    public static AuthenticatedUser founded(final UserId userId,
                                            final Credentials credentials) {
            return new AuthenticatedUser(userId, credentials);
    }

    private void setUserId(final UserId aUserId) {
        Objects.requireNonNull(aUserId, "UserId must not be null");
        this.userId = aUserId;
    }

    private void setUserCredentials(final Credentials aUserCredentials) {
        Objects.requireNonNull(aUserCredentials,
                "UserCredentials must not be null");
        this.userCredentials = aUserCredentials;
    }
}
