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
    @NotEmpty(message = "Не заполнен email")
    @Email(message = "Некорректная почта")
    private String userEmail;

    @NotEmpty(message = "Не заполнен пароль")
    @Size(min = 8, message = "Длина пароля должна быть не менее 8")
    private String userPassword;

    public Credentials toCredentials() {
        return new Credentials(userEmail, userPassword);
    }
}
