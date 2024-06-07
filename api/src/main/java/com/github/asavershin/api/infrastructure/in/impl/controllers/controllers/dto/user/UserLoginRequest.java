package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.user;

import com.github.asavershin.api.domain.user.Credentials;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {
    /**
     * Minimum password length.
     */
    private static final int MIN_PASSWORD_LENGTH = 8;
    /**
     * User email using as login.
     */
    @NotEmpty(message = "Не заполнен email")
    @Email(message = "Некорректная почта")
    private String userEmail;

    /**
     * User password.
     */
    @NotEmpty(message = "Не заполнен пароль")
    @Size(min = MIN_PASSWORD_LENGTH,
            message = "Длина пароля должна быть не менее 8")
    private String userPassword;

    /**
     * Fabric using for mapping DTO to credentials.
     * @return User credentials
     */
    public final Credentials toCredentials() {
        return new Credentials(userEmail, userPassword);
    }
}
