package com.github.asavershin.api.infrastructure.out.persistence;

import com.github.asavershin.api.domain.user.AuthenticatedUser;
import com.github.asavershin.api.domain.user.AuthenticatedUserRepository;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;

import static asavershin.generated.package_.tables.Users.USERS;

@Repository
@RequiredArgsConstructor
public class AuthenticatedUserRepositoryImpl implements AuthenticatedUserRepository, RecordMapper<Record, AuthenticatedUser> {
    private final DSLContext dslContext;
    @Override
    public AuthenticatedUser findByEmail(String email) {
        return dslContext.select(
                        USERS.USER_ID,
                        USERS.USER_EMAIL,
                        USERS.USER_PASSWORD
                        )
                        .from(USERS)
                        .where(USERS.USER_EMAIL.eq(email))
                        .fetchOne(this);
    }


    @Override
    public AuthenticatedUser map(Record record) {
        var userId = record.get(USERS.USER_ID);
        var email = record.get(USERS.USER_EMAIL);
        var password = record.get(USERS.USER_PASSWORD);
        return AuthenticatedUser.founded(new UserId(userId), new Credentials(email, password));
    }
}
