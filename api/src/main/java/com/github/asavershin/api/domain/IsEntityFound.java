package com.github.asavershin.api.domain;

import com.github.asavershin.api.common.Validator;

public abstract class IsEntityFound {
    protected final void isEntityFound(final Object entity,
                                       final String entityName,
                                       final String idName,
                                       final String entityId) {
        Validator.assertNotFound(entity,
                entityName + " with " + idName + entityId + " not found");
    }
}
