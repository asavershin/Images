package com.github.asavershin.api.infrastructure.in.controllers.dto.user;


import com.github.asavershin.api.config.properties.UserProperties;
import com.github.asavershin.api.domain.user.Credentials;
import com.github.asavershin.api.domain.user.FullName;
import com.github.asavershin.api.domain.user.User;
import com.github.asavershin.api.domain.user.UserId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
public class UserRegistrationRequest {


    /**
     * User firstname.
     */
    @NotEmpty(message = "Не заполнено имя")
    @Size(min = 1, max = UserProperties.MAX_FIRSTNAME_LENGTH,
            message = "Недопустимая длина имени")
    private String userFirstname;

    /**
     * User lastname.
     */
    @NotEmpty(message = "Не заполнена фамилия")
    @Size(min = 1, max = UserProperties.MAX_LASTNAME_LENGTH,
            message = "Недопустимая длина фамилии")
    private String userLastname;
    /**
     * User email using as login.
     */
    @NotEmpty(message = "Не заполнен email")
    @Email(message = "Некорректная почта")
    @Getter
    private String userEmail;

    /**
     * User password.
     */
    @NotEmpty(message = "Не заполнен пароль")
    @Size(min = UserProperties.MIN_PASSWORD_LENGTH,
            message = "Длина пароля должна быть не менее 8")
    @Getter
    private String userPassword;
    /**
     * Converts the UserRegistrationRequest DTO to a User domain object.
     *
     * @return A new User object with the provided data.
     */
    public User toUser() {
        return new User(
                UserId.nextIdentity(),
                toFullName(),
                toCredentials()
        );
    }

    /**
     * Fabric method to create FullName from DTO.
     *
     * @return FullName value object
     */
    private FullName toFullName() {
        return new FullName(userFirstname, userLastname);
    }

    /**
     * Fabric method to create credentials.
     *
     * @return Credentials value object
     */
    private Credentials toCredentials() {
        return new Credentials(userEmail, userPassword);
    }
}
