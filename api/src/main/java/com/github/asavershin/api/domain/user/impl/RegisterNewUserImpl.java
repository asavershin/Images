package com.github.asavershin.api.domain.user.impl;

import com.github.asavershin.api.common.annotations.Command;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.asavershin.api.domain.user.*;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import lombok.RequiredArgsConstructor;


@Command
@RequiredArgsConstructor
public class RegisterNewUserImpl implements RegisterNewUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(FullName fullName, Credentials credentials) {
        checkEmailForUnique(credentials.email());
        userRepository.save(
                new User(
                        UserId.nextIdentity(),
                        fullName,
                        new Credentials(credentials.email(), protectPassword(credentials.password()))
                )
        );
    }

    private void checkEmailForUnique(String email) {
        if (userRepository.existByUserEmail(email)) {
            throw new IllegalArgumentException("Email is not unique");
        }
    }

    private String protectPassword(String unprotectedPassword) {
        return passwordEncoder.encode(unprotectedPassword);
    }
}
