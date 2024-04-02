package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {

    private String message;
    private Map<String, Object> errors;

    public ExceptionBody(
            final String message
    ) {
        this.message = message;
    }

}
