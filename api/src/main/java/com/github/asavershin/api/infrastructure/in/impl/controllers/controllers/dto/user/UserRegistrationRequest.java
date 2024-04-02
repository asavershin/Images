package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.user;


import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.FullName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
public class UserRegistrationRequest {

    @NotEmpty(message = "Не заполнено имя")
    @Size(min = 1, max = 20, message = "Недопустимая длина имени")
    private String userFirstname;

    @NotEmpty(message = "Не заполнена фамилия")
    @Size(min = 1, max = 20, message = "Недопустимая длина фамилии")
    private String userLastname;

    @NotEmpty(message = "Не заполнен email")
    @Email(message = "Некорректная почта")
    @Getter
    private String userEmail;

    @NotEmpty(message = "Не заполнен пароль")
    @Size(min = 8, message = "Длина пароля должна быть не менее 8")
    @Getter
    private String userPassword;

    public FullName ToFullName() {
        return new FullName(userFirstname, userLastname);
    }

    public Credentials toCredentials() {
        return new Credentials(userEmail, userPassword);
    }
}
