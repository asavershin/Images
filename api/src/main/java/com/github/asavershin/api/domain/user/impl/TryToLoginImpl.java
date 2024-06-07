package com.github.asavershin.api.domain.user.impl;

import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.user.AuthException;
import com.github.asavershin.api.common.annotations.DomainService;
import com.github.asavershin.api.domain.user.AuthenticatedUser;
import com.github.asavershin.api.domain.user.AuthenticatedUserRepository;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.TryToLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@DomainService
@RequiredArgsConstructor
public class TryToLoginImpl extends IsEntityFound implements TryToLogin {
    /**
     * The {@link PasswordEncoder}
     * is used to encode and decode passwords securely.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * The {@link AuthenticatedUserRepository} is used
     * to retrieve user data from the database.
     */
    private final AuthenticatedUserRepository authenticatedUserRepository;
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public AuthenticatedUser login(final Credentials credentials) {

        var authenticatedUser = authenticatedUserRepository
                .findByEmail(credentials.email());

        isEntityFound(authenticatedUser, "User", "email", credentials.email());
        if (passwordEncoder.matches(credentials.password(),
                authenticatedUser.userCredentials().password())) {
            return authenticatedUser;
        }
        throw new AuthException("Wrong password");
    }
}
