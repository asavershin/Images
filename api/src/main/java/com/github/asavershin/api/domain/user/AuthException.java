package com.github.asavershin.api.domain.user;

public class AuthException extends RuntimeException {
    public AuthException(String message){
        super(message);
    }
}
