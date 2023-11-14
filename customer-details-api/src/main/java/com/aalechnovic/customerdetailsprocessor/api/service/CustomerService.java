package com.aalechnovic.customerdetailsprocessor.api.service;

import com.aalechnovic.customerdetailsprocessor.api.persistence.Customer;
import com.aalechnovic.customerdetailsprocessor.api.persistence.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> save(Customer customer) {
        return customerRepository.findById(customer.getCustomerId())
                                 .flatMap(existingCustomer -> Mono.<Customer>error(new CustomerAlreadyExistsException(existingCustomer.getCustomerId())))
                                 .switchIfEmpty(Mono.defer(() -> customerRepository.save(customer)));

    }

    public Mono<Customer> findByRef(String ref){
        return customerRepository.findById(ref);
    }
}
