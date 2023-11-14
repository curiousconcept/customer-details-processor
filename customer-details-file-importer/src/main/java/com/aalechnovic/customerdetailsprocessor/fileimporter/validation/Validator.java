package com.aalechnovic.customerdetailsprocessor.fileimporter.validation;


public interface Validator {
    <T> void validate(T object);
}
