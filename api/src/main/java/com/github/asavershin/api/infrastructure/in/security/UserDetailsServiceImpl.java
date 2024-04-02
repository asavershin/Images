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
public class UserDetailsServiceImpl extends IsEntityFound implements UserDetailsService {
    private final AuthenticatedUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var authenticatedUser = repository.findByEmail(username);
        isEntityFound(authenticatedUser, "User", "email", username);
        return new CustomUserDetails(authenticatedUser);
    }
}
