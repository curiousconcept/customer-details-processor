package com.aalechnovic.customerdetailsprocessor.fileimporter.validation;

import jakarta.validation.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidatorImpl implements Validator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final jakarta.validation.Validator validator = factory.getValidator();

    @Override
    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Error during validation " + object.getClass().getSimpleName() + " - offending fields: {");

            for (ConstraintViolation<T> violation : violations) {
                errorMessage.append("[");
                errorMessage.append("\"").append(violation.getPropertyPath()).append("\" : \"").append(violation.getMessage());
                errorMessage.append("\"]");
            }

            errorMessage.append("}");

            throw new ValidationException(errorMessage.toString());
        }
    }
}
