package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers;

import com.github.asavershin.api.common.NotFoundException;
import com.github.asavershin.api.domain.ResourceOwnershipException;
import com.github.asavershin.api.domain.user.AuthException;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.ExceptionBody;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.UISuccessContainer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class AdviceController {

    /**
     * Not final to allows Spring use proxy.
     *
     * @param ex is handled exception
     * @return Exception representation for user.
     */
    @ExceptionHandler({ResourceOwnershipException.class, AuthException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public UISuccessContainer handleValidationException(
            final RuntimeException ex
    ) {
        return new UISuccessContainer(false, ex.getMessage());
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param ex is handled exception
     * @return Exception representation for user.
     */
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UISuccessContainer handleNotFoundException(
            final RuntimeException ex
    ) {
        return new UISuccessContainer(false, ex.getMessage());
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param ex is handled exception
     * @return Exception representation for user.
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public UISuccessContainer handleInnerException(final Exception ex) {
        ex.printStackTrace();
        return new UISuccessContainer(false, "Very bad exception");
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param ex is handled exception
     * @return Exception representation for user.
     */
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UISuccessContainer handleIllegalImageExtension(
            final IllegalArgumentException ex
    ) {
        return new UISuccessContainer(false, ex.getMessage());
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param ex is handled exception
     * @return Exception representation for user.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleValidationException(
            final MethodArgumentNotValidException ex
    ) {
        var exceptionBody = new ExceptionBody("Validation failed: ");
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getBindingResult().getAllErrors().stream()
                        .map(
                                DefaultMessageSourceResolvable
                                        ::getDefaultMessage
                        )
                        .filter(Objects::nonNull)
                        .toList());
        exceptionBody.setErrors(body);
        return exceptionBody;
    }

    /**
     * Not final to allows Spring use proxy.
     *
     * @param ex is handled exception
     * @return Exception representation for user.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolationException(
            final ConstraintViolationException ex
    ) {
        var exceptionBody = new ExceptionBody("Validation failed: ");
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .filter(Objects::nonNull)
                        .toList());
        exceptionBody.setErrors(body);
        return exceptionBody;
    }
}
