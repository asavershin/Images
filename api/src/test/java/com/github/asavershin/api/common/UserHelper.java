package com.github.asavershin.api.common;

import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.FullName;
import com.github.asavershin.api.domain.user.User;
import com.github.asavershin.api.domain.user.UserId;

public class UserHelper {
    public static FullName fullName1(){
        return new FullName("Alexander", "Avershin");
    }

    public static FullName fullName2(){
        return new FullName("Avershin", "Alexander");
    }

    public static Credentials credentials1(){
        return new Credentials("test@test.test", "verysecretpassword");
    }

    public static Credentials credentials2(){
        return new Credentials("test2@test.test", "verysecretpassword");
    }

    public static Credentials invalidEmail(){
        return new Credentials("invalid", "verysecretpassword");
    }

    public static UserId UserId(){
        return UserId.nextIdentity();
    }

    public static User user1(UserId userId, FullName userFullName, Credentials userCredentials){
        return new User(userId, userFullName, userCredentials);
    }

    public static FullName longFirstName(){
        return new FullName("TooLongFirstNameTooLongFirTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNamestNameTooLongFirstNameTooLongFirstName",
                "Lastname");
    };

    public static FullName longLastName(){
        return new FullName("Firstname",
                "TooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongFirstNameTooLongLastNameTooLongLastNameTooLongLastNameTooLongLastName");
    };
}
