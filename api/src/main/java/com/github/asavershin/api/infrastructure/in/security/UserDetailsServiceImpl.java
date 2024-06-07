package com.github.asavershin.api.infrastructure.in.security;

import com.github.asavershin.api.domain.IsEntityFound;
import com.github.asavershin.api.domain.user.AuthenticatedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl
        extends IsEntityFound implements UserDetailsService {
    /**
     * The AuthenticatedUserRepository
     * dependency is used to fetch the User entity from the database.
     */
    private final AuthenticatedUserRepository repository;
    /**
     * The loadUserByUsername method is an implementation
     * of the UserDetailsService interface.
     * It is responsible for loading a UserDetails object
     * based on the provided username.
     * Not final to allow spring use proxy.
     *
     * @param username The username of the User to be loaded.
     * @return A UserDetails object
     * representing the User with the provided username.
     * @throws UsernameNotFoundException If the User with
     * the provided username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(
            final String username
    ) throws UsernameNotFoundException {
        var authenticatedUser = repository.findByEmail(username);
        isEntityFound(authenticatedUser, "User", "email", username);
        return new CustomUserDetails(authenticatedUser);
    }
}
