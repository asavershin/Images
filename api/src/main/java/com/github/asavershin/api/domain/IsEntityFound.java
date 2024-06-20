package com.github.asavershin.api.domain;

import com.github.asavershin.api.common.NotFoundException;
import com.github.asavershin.api.common.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class IsEntityFound {
    protected final <T> T isEntityFound(final T entity,
                                       final String entityName,
                                       final String idName,
                                       final String entityId) {
        return Validator.assertNotFound(entity,
                entityName + " with " + idName + entityId + " not found");
    }

    protected final void isEntityFound(final Runnable saveFunction,
                                       final String entityName,
                                       final String idName,
                                       final String entityId) {
        try {
            saveFunction.run();
        } catch (RuntimeException e) {
            log.info("Error load entity: {}", e.getMessage());
            throw new NotFoundException(
                    entityName + " with "
                            + idName + " " + entityId + " maybe not found");
        }
    }
}
