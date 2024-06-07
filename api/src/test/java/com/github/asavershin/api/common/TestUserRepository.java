package com.github.asavershin.api.common;


import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.FullName;
import com.github.asavershin.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectConditionStep;
import org.springframework.stereotype.Repository;
import com.github.asavershin.api.domain.user.User;

import java.util.UUID;

import static asavershin.generated.package_.tables.Users.USERS;

@Repository
@RequiredArgsConstructor
public class TestUserRepository implements RecordMapper<Record, User> {
    private final DSLContext dslContext;
    public SelectConditionStep<Record> findUserById(UUID id){
        return dslContext.select()
                .from(USERS)
                .where(USERS.USER_ID.eq(id));
    }

    public Long countUsers(){
        return dslContext.selectCount()
                .from(USERS)
                .fetchOne(0, Long.class);
    }

    @Override
    public @Nullable User map(Record record) {
        var userId = record.get(USERS.USER_ID);
        var email = record.get(USERS.USER_EMAIL);
        var password = record.get(USERS.USER_PASSWORD);
        var firstName = record.get(USERS.USER_FIRSTNAME);
        var lastName = record.get(USERS.USER_LASTNAME);
        return new User(new UserId(userId),
                new FullName(firstName, lastName),
                new Credentials(email, password));
    }
}
