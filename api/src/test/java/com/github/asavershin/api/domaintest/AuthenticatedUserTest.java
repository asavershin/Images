package com.github.asavershin.api.domaintest;

import com.github.asavershin.api.common.UserHelper;
import com.github.asavershin.api.domain.user.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticatedUserTest {

    @Test
    public void testFoundedUser() {
        UserId userId = UserHelper.userId();
        Credentials credentials = UserHelper.credentials1();
        AuthenticatedUser authenticatedUser = AuthenticatedUser.founded(userId, credentials);
        assertEquals(userId, authenticatedUser.userId());
        assertEquals(credentials, authenticatedUser.userCredentials());
    }

    @Test
    public void testFoundedUserWithEmailEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AuthenticatedUser.founded(UserHelper.userId(), new Credentials("", "password"));
        });
        assertEquals("Email must be 5-50 in length", exception.getMessage());
    }

    @Test
    public void testFoundedUserWithPasswordNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            AuthenticatedUser.founded(UserHelper.userId(), new Credentials("test@test.com", null));
        });
        assertEquals("Password must not be null", exception.getMessage());
    }

    @Test
    public void testFoundedUserWithPasswordTooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AuthenticatedUser.founded(UserHelper.userId(), new Credentials("test@test.com", "short"));
        });
        assertEquals("Password must be greater than 8", exception.getMessage());
    }



}

