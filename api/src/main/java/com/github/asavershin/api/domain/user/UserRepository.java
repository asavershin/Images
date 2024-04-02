package com.github.asavershin.api.domain.user;

import java.util.Optional;

public interface UserRepository {
    void save(User newUser);

    boolean existByUserEmail(String email);
}
