package io.dxheroes.endpointsmonitoringservice.controller.v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String conditionName, Object conditionValue){
        super("No entity found "+clazz.getName()+" for condition "+conditionName+"="+conditionValue);

    }

}
