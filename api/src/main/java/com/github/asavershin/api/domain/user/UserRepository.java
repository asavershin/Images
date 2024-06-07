package com.github.asavershin.api.domain.user;

/**
 * UserRepository interface provides methods to interact with the User
 * data.
 *
 * @author Tabnine
 */
public interface UserRepository {

    /**
     * Saves a new User object to the repository.
     *
     * @param newUser the User object to be saved
     */
    void save(User newUser);

    /**
     * Checks if a User with the given email already exists in the
     * repository.
     *
     * @param email the email of the User to be checked
     * @return true if a User with the given email already exists,
     * false otherwise
     */
    boolean existByUserEmail(String email);
}
