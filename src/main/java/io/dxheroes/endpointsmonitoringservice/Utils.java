package io.dxheroes.endpointsmonitoringservice;

import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationException;
import io.dxheroes.endpointsmonitoringservice.dto.ValidationErrorDTO;
import org.apache.commons.validator.routines.UrlValidator;

public class Utils {

    public static boolean isValidUUID(String text){
        return text.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
    }

    public static boolean isValidEmail(String text){
        return text.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidURL(String text){
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(text);
    }

    public static void validateAccessToken(String accessToken){
        validateAccessToken(accessToken, null);
    }

    public static void validateAccessToken(String accessToken, Class clazz){
        if(accessToken != null && !Utils.isValidUUID(accessToken)){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().invalidValue(accessToken)
                    .propertyPath("accessToken")
                    .clazz(clazz)
                    .errorCode(ValidationErrorConstants.INVALID_ACCESS_TOKEN)
                    .build();
            throw new ValidationException(validationErrorDTO);
        }
    }


}
