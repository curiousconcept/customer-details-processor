package com.aalechnovic.customerdetailsprocessor.api.web.error;

import com.aalechnovic.customerdetailsprocessor.api.service.CustomerAlreadyExistsException;
import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHttpResponseTranslator {
    
    private static final String fieldName = Objects.requireNonNull(ReflectionUtils.findField(CusDetails.class, "customerRef")).getName();

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleConflict(CustomerAlreadyExistsException ex) {

        String errorMessage = ex.getMessage();

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put(ExceptionHttpResponseTranslator.camelCaseToSnakeCase(fieldName), errorMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String,String>> handleException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                      .getAllErrors()
                      .stream()
                      .map(FieldError.class::cast)
                      .collect(Collectors.toMap(er-> camelCaseToSnakeCase(er.getField()), er -> Optional.ofNullable(er.getDefaultMessage()).orElse("")));

        return ResponseEntity.badRequest().body(errors);
    }

    private static String camelCaseToSnakeCase(String camelCase) {
        StringBuilder snakeCase = new StringBuilder();

        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                snakeCase.append('_').append(Character.toLowerCase(c));
            } else {
                snakeCase.append(c);
            }
        }

        return snakeCase.toString();
    }
}