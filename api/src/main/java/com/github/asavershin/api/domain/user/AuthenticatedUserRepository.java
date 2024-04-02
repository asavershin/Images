package com.github.asavershin.api.domain.user;

import java.util.Optional;

public interface AuthenticatedUserRepository {
    AuthenticatedUser findByEmail(String email);
}
