package com.github.asavershin.api.domain;

import com.github.asavershin.api.common.Validator;

public abstract class IsEntityFound {
    protected void isEntityFound(Object entity, String entityName, String idName, String entityId){
        Validator.assertNotFound(entity,
                entityName + " with " + idName + entityId + " not found");
    }
}
