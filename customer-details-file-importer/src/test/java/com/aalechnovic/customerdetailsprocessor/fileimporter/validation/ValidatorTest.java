package com.aalechnovic.customerdetailsprocessor.fileimporter.validation;

import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    private final Validator validator = new ValidatorImpl();

    @Test
    void validateObjectWithValidFields_shouldNotThrowException() {
        CusDetails validCusDetails = new CusDetails("de29fb90-12c4-11e1-840d-7b25c5ee775a", "Doe", "Doe", "Doe", "Doe", "Doe", "Doe", "Doe");
        assertDoesNotThrow(() -> validator.validate(validCusDetails));
    }

    @Test
    void validateObjectWithInvalidFields_shouldThrowValidationException() {
        CusDetails invalidCusDetails = new CusDetails("de29fb90", "", "Doe", "Doe", "Doe", "Doe", "Doe", "Doe");
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(invalidCusDetails));

        assertThat(exception).hasMessageContaining("Error during validation CusDetails - offending fields");
        assertThat(exception).hasMessageContaining("[\"customerName\" : \"size must be between 1 and 255\"]");
        assertThat(exception).hasMessageContaining("[\"customerRef\" : \"size must be between 36 and 36\"]");
    }
}