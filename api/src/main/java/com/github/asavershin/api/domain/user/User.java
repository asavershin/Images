/**
 * User class represents a user entity in the system.
 * It contains the user's unique identifier, full name, and credentials.
 *
 * @author asavershin
 */
package com.github.asavershin.api.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@EqualsAndHashCode
@Getter
public class User {
    /**
     * The unique identifier for the user.
     */
    private UserId userId;

    /**
     * The full name of the user.
     */
    private FullName userFullName;

    /**
     * The credentials of the user.
     */
    private Credentials userCredentials;

    /**
     * Constructor for User class.
     *
     * @param id          the unique identifier for the user
     * @param fullname    the full name of the user
     * @param credentials the credentials of the user
     */
    public User(final UserId id,
                final FullName fullname,
                final Credentials credentials) {
        setUserId(id);
        setUserCredentials(credentials);
        setUserFullName(fullname);
    }

    private void setUserId(final UserId id) {
        Objects.requireNonNull(id,
                "UserId must not be null");
        this.userId = id;
    }

    private void setUserFullName(final FullName fullName) {
        Objects.requireNonNull(fullName,
                "UserFullName must not be null");
        this.userFullName = fullName;
    }

    private void setUserCredentials(final Credentials credentials) {
        Objects.requireNonNull(credentials,
                "UserCredentials must not be null");
        this.userCredentials = credentials;
    }

    /**
     * Method to update the user's credentials.
     * This method is used to protect the user's
     * password by updating it with a new one.
     *
     * @param credentials the new credentials to be set for the user with
     *                    encoded password
     * @return User with encoded password
     */
    public User protectPassword(final Credentials credentials) {
        setUserCredentials(credentials);
        return this;
    }
}
