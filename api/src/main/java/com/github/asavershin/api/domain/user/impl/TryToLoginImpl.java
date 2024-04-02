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

    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserRepository authenticatedUserRepository;

    @Override
    public AuthenticatedUser login(Credentials credentials) {

        var authenticatedUser = authenticatedUserRepository.findByEmail(credentials.email());

        isEntityFound(authenticatedUser,"User", "email", credentials.email());
        if(passwordEncoder.matches(credentials.password(), authenticatedUser.userCredentials().password())) {
            return authenticatedUser;
        }
        throw new AuthException("Wrong password");
    }
}
