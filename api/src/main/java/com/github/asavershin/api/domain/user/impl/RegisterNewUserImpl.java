package com.github.asavershin.api.domain.user.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import com.github.asavershin.api.domain.user.User;
import com.github.asavershin.api.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@Command
@RequiredArgsConstructor
public class RegisterNewUserImpl implements RegisterNewUser {
    /**
     * Dependency that allow crud with User entity.
     */
    private final UserRepository userRepository;
    /**
     * is used to encode and decode passwords securely.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void register(final User newUser) {
        userRepository.save(
                checkEmailForUnique(
                        newUser
                ).protectPassword(
                        encode(newUser.userCredentials())
                )
        );
    }

    private User checkEmailForUnique(final User newUser) {
        if (userRepository.existByUserEmail(
                newUser.userCredentials().email())
        ) {
            throw new IllegalArgumentException("Email is not unique");
        }
        return newUser;
    }
    private Credentials encode(final Credentials unprotected) {
        return new Credentials(
                unprotected.email(),
                passwordEncoder.encode(
                        unprotected.password()
                )
        );
    }
}
