package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UISuccessContainer {
    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;
    /**
     * The message to be displayed to the user.
     */
    private String message;
}
