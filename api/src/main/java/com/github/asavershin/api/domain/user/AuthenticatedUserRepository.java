package com.github.asavershin.api.domain.user;

/**
 * Represents a repository for finding authenticated users by their email.
 */
public interface AuthenticatedUserRepository {
    /**
     * Finds an authenticated user by their email.
     *
     * @param email the email of the user to find
     * @return the authenticated user with the given email, or null if not found
     */
    AuthenticatedUser findByEmail(String email);
}
