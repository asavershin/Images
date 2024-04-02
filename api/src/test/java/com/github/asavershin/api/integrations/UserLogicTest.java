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
    private GetNewCredentialsUsingRefreshTokenImpl getNewCredentialsUsingRefreshToken;



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
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        var fullName2 = UserHelper.fullName1();
        var credentials2 = UserHelper.credentials2();

        // When
        registerNewUser.register(fullName, credentials);
        registerNewUser.register(fullName2, credentials2);


        // Then
        assertEquals(2, testUserRepository.countUsers());
        assertNotNull(authenticatedUserRepository.findByEmail(credentials.email()));
        assertNotNull(authenticatedUserRepository.findByEmail(credentials2.email()));

    }

    @Test
    public void testRegisterNewUserWithoutUniqueEmail(){
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();

        // When
        registerNewUser.register(fullName, credentials);

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registerNewUser.register(UserHelper.fullName1(), UserHelper.credentials1());
        });

        assertEquals("Email is not unique", exception.getMessage());
    }

    @Test
    public void testGetNewCredentials(){
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();

        // When
        registerNewUser.register(fullName, credentials);
        var newCredentials = getNewCredentials.get(credentials);
        assertDoesNotThrow(() -> jwtService.extractSub(newCredentials.getAccessToken()));
        assertDoesNotThrow(() -> jwtService.extractSub(newCredentials.getRefreshToken()));

        var access = jwtService.extractSub(newCredentials.getAccessToken());
        var refresh = jwtService.extractSub(newCredentials.getRefreshToken());

        assertDoesNotThrow(() -> tokenRepository.getAccessToken(access));
        assertDoesNotThrow(() -> tokenRepository.getRefreshToken(refresh));

        var accessFromRedis = tokenRepository.getAccessToken(access);
        var refreshFromRedis = tokenRepository.getRefreshToken(refresh);

        // Then
        assertNotNull(access);
        assertNotNull(refresh);
        assertEquals(credentials.email(), access);
        assertEquals(credentials.email(), refresh);

        assertNotNull(accessFromRedis);
        assertNotNull(refreshFromRedis);

        assertEquals(newCredentials.getAccessToken(), accessFromRedis);
        assertEquals(newCredentials.getRefreshToken(), refreshFromRedis);
    }

    @Test
    public void testGetNewCredentialsForNotRegisteredUser(){
        // When
        registerNewUser.register(UserHelper.fullName1(), UserHelper.credentials1());
        var ex = assertThrows(NotFoundException.class, () -> getNewCredentials.get(UserHelper.credentials2()));

        // Then
        assertEquals(ex.getMessage(),
                "User" + " with " + "email" + UserHelper.credentials2().email() + " not found");
    }

    @Test
    public void getNewCredentialsUsingRefreshTokenTest() throws InterruptedException {
        // Given
        var fullName = UserHelper.fullName1();
        var credentials = UserHelper.credentials1();
        registerNewUser.register(fullName, credentials);
        var newCredentials = getNewCredentials.get(credentials);

        // When
        Thread.sleep(2000L);
        var refreshedCredentials = getNewCredentialsUsingRefreshToken.get(credentials);

        // Then
        assertNotNull(refreshedCredentials);
        assertNotEquals(newCredentials.getAccessToken(), refreshedCredentials.getAccessToken());
        assertNotEquals(newCredentials.getRefreshToken(), refreshedCredentials.getRefreshToken());
    }
}
