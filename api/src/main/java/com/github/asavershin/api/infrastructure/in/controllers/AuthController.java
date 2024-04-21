package com.github.asavershin.api.infrastructure.in.controllers;

import com.github.asavershin.api.application.in.services.user.ApplicationCredentials;
import com.github.asavershin.api.application.in.services.user.GetNewCredentials;
import com.github.asavershin.api.application.in.services.user.GetNewCredentialsUsingRefreshToken;
import com.github.asavershin.api.domain.user.RegisterNewUser;
import com.github.asavershin.api.infrastructure.in.controllers.dto.user.UserLoginRequest;
import com.github.asavershin.api.infrastructure.in.controllers.dto.user.UserRegistrationRequest;
import com.github.asavershin.api.infrastructure.in.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    /**
     * Dependency that allows to register new user.
     */
    private final RegisterNewUser register;
    /**
     * Dependency that allows to get new tokens during authentication.
     */
    private final GetNewCredentials getNewCredentials;
    /**
     * Dependency that allows to get new tokens using refresh token.
     */
    private final GetNewCredentialsUsingRefreshToken getRefreshToken;

    /**
     * Not final to allows Spring use proxy.
     * @param request DTO that contains user data like email, password
     *                firstname, etc.
     */
    @PostMapping("/register")
    @Operation(description = "Регистрация нового пользователя")
    public void register(
            final @RequestBody @Valid UserRegistrationRequest request
    ) {
        register.register(request.toUser());
    }

    /**
     * Not final to allows Spring use proxy.
     * @param userLoginRequest DTO that represents login, password.
     * @return DTO that contains access, refresh tokens
     */
    @PostMapping("/login")
    @Operation(description = "Аутентификация пользователя")
    public ApplicationCredentials login(
            final @RequestBody @Valid UserLoginRequest userLoginRequest
    ) {
        return getNewCredentials.get(userLoginRequest.toCredentials());
    }
    /**
     * Not final to allows Spring use proxy.
     * @param user Is param that injects by spring and contains
     *             current authenticated spring user.
     * @return DTO that contains access, refresh tokens.
     */
    @PostMapping("/refresh-token")
    @Operation(description = "Использовать рефреш токен")
    public ApplicationCredentials refreshToken(
            final @AuthenticationPrincipal CustomUserDetails user
    ) {
        return getRefreshToken.get(user.authenticatedUser().userCredentials());
    }
}
