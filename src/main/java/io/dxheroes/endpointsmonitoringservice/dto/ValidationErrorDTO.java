package io.dxheroes.endpointsmonitoringservice.dto;

import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationErrorDTO {

    private Class clazz;

    private String propertyPath;

    private Object invalidValue;

    private ValidationErrorConstants errorCode;

}
