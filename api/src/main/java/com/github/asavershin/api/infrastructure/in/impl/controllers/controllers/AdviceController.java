package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers;

import com.github.asavershin.api.common.NotFoundException;
import com.github.asavershin.api.domain.ResourceOwnershipException;
import com.github.asavershin.api.domain.user.AuthException;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.ExceptionBody;
import com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto.UISuccessContainer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class AdviceController {


    @ExceptionHandler({ResourceOwnershipException.class, AuthException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public UISuccessContainer handleValidationException(RuntimeException ex) {
        return new UISuccessContainer(false, ex.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UISuccessContainer handleNotFoundException(RuntimeException ex) {
        return new UISuccessContainer(false, ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public UISuccessContainer handleInnerException(Exception ex) {
        return new UISuccessContainer(false, "Very bad exception");
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UISuccessContainer handleIllegalImageExtension(IllegalArgumentException ex) {
        log.info("IllegaArgumentException: " + ex);
        return new UISuccessContainer(false, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleValidationException(MethodArgumentNotValidException ex) {
        log.info("handleValidationException");
        var exceptionBody = new ExceptionBody("Validation failed: ");
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).filter(Objects::nonNull)
                        .toList());
        exceptionBody.setErrors(body);
        return exceptionBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolationException(ConstraintViolationException ex) {
        log.info("handleConstraintViolationException");
        var exceptionBody = new ExceptionBody("Validation failed: ");
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).filter(Objects::nonNull)
                        .toList());
        exceptionBody.setErrors(body);
        return exceptionBody;
    }
}
