package com.github.asavershin.api.infrastructure.in.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {
    /**
     * Message for user about exception.
     */
    private String message;
    /**
     * Map of error for a lot of exceptions.
     */
    private Map<String, Object> errors;
    /**
     * Constructs an instance of {@link ExceptionBody}
     * with the provided message.
     *
     * @param aMessage Message for user about exception.
     */
    public ExceptionBody(
            final String aMessage
    ) {
        this.message = aMessage;
    }

}
