package com.github.asavershin.api.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@EqualsAndHashCode
public class User{
    @Getter
    private UserId userId;
    @Getter
    private FullName userFullName;
    @Getter
    private Credentials userCredentials;

    public User(UserId userId, FullName userFullName, Credentials userCredentials) {
        setUserId(userId);
        setUserCredentials(userCredentials);
        setUserFullName(userFullName);
    }

    private void setUserId(UserId userId) {
        Objects.requireNonNull(userId, "UserId must not be null");
        this.userId = userId;
    }

    private void setUserFullName(FullName userFullName) {
        Objects.requireNonNull(userFullName, "UserFullName must not be null");
        this.userFullName = userFullName;
    }

    private void setUserCredentials(Credentials userCredentials) {
        Objects.requireNonNull(userCredentials, "UserCredentials must not be null");
        this.userCredentials = userCredentials;
    }
}
