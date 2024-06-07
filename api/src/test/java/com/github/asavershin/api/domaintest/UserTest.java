package com.github.asavershin.api.domaintest;

import com.github.asavershin.api.common.UserHelper;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.FullName;
import com.github.asavershin.api.domain.user.User;
import com.github.asavershin.api.domain.user.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testCreateNewUserForRegistration() {
        UserId userId = UserHelper.UserId();
        FullName fullName = UserHelper.fullName1();
        Credentials credentials = UserHelper.credentials1();
        User user = new User(userId, fullName, credentials);
        assertEquals(userId, user.userId());
        assertEquals(fullName, user.userFullName());
        assertEquals(credentials, user.userCredentials());
    }

    @Test
    public void testCreateNewUserForRegistrationWithNullUserId() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new User(null, UserHelper.fullName1(), UserHelper.credentials1());
        });
        assertEquals("UserId must not be null", exception.getMessage());
    }

    @Test
    public void testCreateNewUserForRegistrationWithNullFullName() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new User(UserHelper.UserId(), null, UserHelper.credentials1());
        });
        assertEquals("UserFullName must not be null", exception.getMessage());
    }

    @Test
    public void testCreateNewUserForRegistrationWithNullCredentials() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new User(UserHelper.UserId(), UserHelper.fullName1(), null);
        });
        assertEquals("UserCredentials must not be null", exception.getMessage());
    }

    @Test
    public void testCreateNewUserForRegistrationWithInvalidFullName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User(UserHelper.UserId(), UserHelper.longFirstName(), UserHelper.credentials1());
        });
        assertEquals("Name must be 0-20 in length", exception.getMessage());
    }

    @Test
    void testEquals() {

        var userId = UserHelper.UserId();

        FullName fullName1 = UserHelper.fullName1();
        Credentials credentials1 = UserHelper.credentials1();
        User user1 = new User(userId, fullName1, credentials1);

        FullName fullName2 = UserHelper.fullName1();
        Credentials credentials2 = UserHelper.credentials1();
        User user2 = new User(userId, fullName2, credentials2);

        UserId userId3 = UserId.nextIdentity();
        FullName fullName3 = UserHelper.fullName2();
        Credentials credentials3 = UserHelper.credentials2();
        User user3 = new User(userId3, fullName3, credentials3);

        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user1));

        assertTrue(user1.equals(user1));

        assertFalse(user1.equals(null));


        assertFalse(user1.equals(user3));
        assertFalse(user3.equals(user1));
    }
}
