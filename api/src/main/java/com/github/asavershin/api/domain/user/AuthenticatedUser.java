package com.github.asavershin.api.domain.user;

import com.github.asavershin.api.domain.IsEntityFound;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class AuthenticatedUser {
    private UserId userId;
    private Credentials userCredentials;

    private AuthenticatedUser(UserId userId, Credentials userCredentials) {
        setUserId(userId);
        setUserCredentials(userCredentials);
    }

    public static AuthenticatedUser founded(UserId userId, Credentials credentials) {
            return new AuthenticatedUser(userId, credentials);
    }

    private void setUserId(UserId userId) {
        Objects.requireNonNull(userId, "UserId must not be null");
        this.userId = userId;
    }

    private void setUserCredentials(Credentials userCredentials) {
        Objects.requireNonNull(userCredentials, "UserCredentials must not be null");
        this.userCredentials = userCredentials;
    }
}
