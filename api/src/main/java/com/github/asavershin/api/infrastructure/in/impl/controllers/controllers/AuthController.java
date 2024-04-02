package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers;

import com.github.asavershin.api.application.in.services.user.ApplicationCredentials;
import com.github.asavershin.api.application.in.services.user.GetNewCredentials;
import com.github.asavershin.api.application.in.services.user.GetNewCredentialsUsingRefreshToken;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.user.UserLoginRequest;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.user.UserRegistrationRequest;
import com.github.asavershin.api.infrastructure.in.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "auth", description = "Аутентификация и регистрация")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterNewUser register;
    private final GetNewCredentials getNewCredentials;
    private final GetNewCredentialsUsingRefreshToken getNewCredentialsUsingRefreshToken;

    @PostMapping("/register")
    @Operation(description = "Регистрация нового пользователя")
    public void register(
            @RequestBody @Valid UserRegistrationRequest userRegistrationRequest
    ) {
        register.register(userRegistrationRequest.ToFullName(),
                userRegistrationRequest.toCredentials());
    }

    @PostMapping("/login")
    @Operation(description = "Аутентификация пользователя")
    public ApplicationCredentials login(@RequestBody @Valid UserLoginRequest userLoginRequest) {

        return getNewCredentials.get(userLoginRequest.toCredentials());
    }

    @PostMapping("/refresh-token")
    @Operation(description = "Использовать рефреш токен")
    public ApplicationCredentials refreshToken(@AuthenticationPrincipal CustomUserDetails user) {
        return getNewCredentialsUsingRefreshToken.get(user.authenticatedUser().userCredentials());
    }
}
