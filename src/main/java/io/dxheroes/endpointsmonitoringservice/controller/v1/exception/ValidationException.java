package io.dxheroes.endpointsmonitoringservice.controller.v1.exception;

import io.dxheroes.endpointsmonitoringservice.dto.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static java.util.Collections.singletonList;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationException extends RuntimeException{

    private final List<ValidationErrorDTO> errors;


    public ValidationException(List<ValidationErrorDTO> errors, Throwable cause) {
        super(generateMessage(errors), cause);
        this.errors = errors;
    }

    public ValidationException(ValidationErrorDTO error) {
        this(singletonList(error));
    }


    public ValidationException(List<ValidationErrorDTO> errors) {
        this(errors, null);
    }

    private static String generateMessage(List<ValidationErrorDTO> errors) {
        StringBuilder sb = new StringBuilder("Validation failed with ");
        sb.append(errors.size()).append(" error(s)");
        sb.append(": ");
        for (ValidationErrorDTO error : errors) {
            sb.append("[").append(error).append("] ");
        }
        return sb.toString();
    }

    public List<ValidationErrorDTO> getErrors() {
        return errors;
    }
}
