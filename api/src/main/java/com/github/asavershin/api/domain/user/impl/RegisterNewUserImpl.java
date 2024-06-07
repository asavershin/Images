package com.github.asavershin.api.domain.user.impl;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.FullName;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import com.github.asavershin.api.domain.user.User;
import com.github.asavershin.api.domain.user.UserId;
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
    public void register(final FullName fullName,
                         final Credentials credentials) {
        checkEmailForUnique(credentials.email());
        userRepository.save(
                new User(
                        UserId.nextIdentity(),
                        fullName,
                        new Credentials(credentials.email(),
                                protectPassword(credentials.password()))
                )
        );
    }

    private void checkEmailForUnique(final String email) {
        if (userRepository.existByUserEmail(email)) {
            throw new IllegalArgumentException("Email is not unique");
        }
    }

    private String protectPassword(final String unprotectedPassword) {
        return passwordEncoder.encode(unprotectedPassword);
    }
}
