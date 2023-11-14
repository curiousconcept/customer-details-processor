package com.aalechnovic.customerdetailsprocessor.api.service;

public class CustomerAlreadyExistsException extends RuntimeException{
    public CustomerAlreadyExistsException(String id) {
        super(id);
    }
}
