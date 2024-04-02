package com.github.asavershin.api.infrastructure.in.impl.controllers.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UISuccessContainer {
    private boolean success;
    private String message;
}
