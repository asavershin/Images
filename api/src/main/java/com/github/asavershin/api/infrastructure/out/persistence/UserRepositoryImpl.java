package com.github.asavershin.api.infrastructure.out.persistence;

import asavershin.generated.package_.tables.records.UsersRecord;
import com.github.asavershin.api.domain.user.UserRepository;
import com.github.asavershin.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static asavershin.generated.package_.Tables.USERS;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    /**
     * The DSLContext object is used to interact with the database.
     */
    private final DSLContext dslContext;

    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public void save(final User newUser) {
        UsersRecord userRecord = dslContext.newRecord(USERS);
        userRecord.setUserId(newUser.userId().value());
        userRecord.setUserFirstname(newUser.userCredentials().email());
        userRecord.setUserLastname(newUser.userFullName().lastname());
        userRecord.setUserEmail(newUser.userCredentials().email());
        userRecord.setUserPassword(newUser.userCredentials().password());

        dslContext.insertInto(USERS)
                .set(userRecord)
                .execute();
    }
    /**
     * Not final to allow spring use proxy.
     */
    @Override
    public boolean existByUserEmail(final String email) {
        return dslContext.fetchExists(
                USERS, USERS.USER_EMAIL.eq(email)
        );
    }
}
