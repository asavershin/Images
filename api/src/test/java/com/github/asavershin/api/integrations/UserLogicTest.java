package com.github.asavershin.api.integrations;

import asavershin.generated.package_.tables.Image;
import asavershin.generated.package_.tables.Users;
import com.github.asavershin.api.application.in.services.user.GetNewCredentials;
import com.github.asavershin.api.application.in.services.user.JwtService;
import com.github.asavershin.api.application.in.services.user.impl.GetNewCredentialsUsingRefreshTokenImpl;
import com.github.asavershin.api.application.out.TokenRepository;
import com.github.asavershin.api.common.NotFoundException;
import com.github.asavershin.api.common.TestUserRepository;
import com.github.asavershin.api.common.UserHelper;
import com.github.asavershin.api.domain.user.*;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.asavershin.api.common.UserHelper.user1;
import static com.github.asavershin.api.common.UserHelper.user2;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserLogicTest extends AbstractTest {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    private RegisterNewUser registerNewUser;

    @Autowired
    private TestUserRepository testUserRepository;

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Autowired
    private GetNewCredentials getNewCredentials;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private GetNewCredentialsUsingRefreshTokenImpl byRefresh;



    @BeforeEach
    public void clearDB(){
        dslContext.delete(Users.USERS).execute();
        dslContext.delete(Image.IMAGE).execute();
        tokenRepository.deleteAllTokensByUserEmail(UserHelper.credentials1().email());
        tokenRepository.deleteAllTokensByUserEmail(UserHelper.credentials1().email());
    }

    @Test
    public void testRegisterNewUser(){
        // Given
        var testUser1 = user1();
        var testUser2 = user2();
        // When
        registerNewUser.register(testUser1);
        registerNewUser.register(testUser2);
        // Then
        var user1 = authenticatedUserRepository.findByEmail(testUser1.userCredentials().email());
        var user2 = authenticatedUserRepository.findByEmail(testUser2.userCredentials().email());
        assertEquals(2, testUserRepository.countUsers());
        assertNotNull(user1);
        assertEquals(user1, AuthenticatedUser.founded(testUser1.userId(), testUser1.userCredentials()));
        assertNotNull(user2);
        assertEquals(user2, AuthenticatedUser.founded(testUser2.userId(), testUser2.userCredentials()));
    }

    @Test
    public void testRegisterNewUserWithoutUniqueEmail(){
        // Given
        var testUser = user1();
        // When
        registerNewUser.register(testUser);
        // Then
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            registerNewUser.register(testUser);
        });
        assertEquals("Email is not unique", exception.getMessage());
    }

    @Test
    public void testGetNewCredentials(){
        // Given
        var testUser = user1();
        // When
        registerNewUser.register(user1());
        var newCredentials = getNewCredentials.get(testUser.userCredentials());
        assertDoesNotThrow(() -> jwtService.extractSub(newCredentials.getAccessToken()));
        assertDoesNotThrow(() -> jwtService.extractSub(newCredentials.getRefreshToken()));
        var accessSub = jwtService.extractSub(newCredentials.getAccessToken());
        var refreshSub = jwtService.extractSub(newCredentials.getRefreshToken());
        assertDoesNotThrow(() -> tokenRepository.getAccessToken(accessSub));
        assertDoesNotThrow(() -> tokenRepository.getRefreshToken(refreshSub));
        var accessFromRedis = tokenRepository.getAccessToken(accessSub);
        var refreshFromRedis = tokenRepository.getRefreshToken(refreshSub);
        // Then
        assertNotNull(accessSub);
        assertNotNull(refreshSub);
        assertEquals(testUser.userCredentials().email(), accessSub);
        assertEquals(testUser.userCredentials().email(), refreshSub);

        assertNotNull(accessFromRedis);
        assertNotNull(refreshFromRedis);

        assertEquals(newCredentials.getAccessToken(), accessFromRedis);
        assertEquals(newCredentials.getRefreshToken(), refreshFromRedis);
    }

    @Test
    public void testGetNewCredentialsForNotRegisteredUser(){
        // When
        registerNewUser.register(user1());
        var ex = assertThrows(NotFoundException.class, () -> getNewCredentials.get(UserHelper.credentials2()));
        // Then
        assertEquals(ex.getMessage(),
                "User" + " with " + "email" + UserHelper.credentials2().email() + " not found");
    }

    @Test
    public void getNewCredentialsUsingRefreshTokenTest() throws InterruptedException {
        // Given
        var testUser = user1();
        registerNewUser.register(testUser);
        var newCredentials = byRefresh.get(testUser.userCredentials());
        // When
        Thread.sleep(2000L);
        var refreshedCredentials = byRefresh.get(testUser.userCredentials());
        // Then
        assertNotNull(refreshedCredentials);
        assertNotEquals(newCredentials.getAccessToken(), refreshedCredentials.getAccessToken());
        assertNotEquals(newCredentials.getRefreshToken(), refreshedCredentials.getRefreshToken());
    }
}
