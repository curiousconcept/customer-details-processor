package com.aalechnovic.customerdetailsprocessor.api.web;


import com.aalechnovic.customerdetailsprocessor.api.persistence.Customer;
import com.aalechnovic.customerdetailsprocessor.api.service.CustomerAlreadyExistsException;
import com.aalechnovic.customerdetailsprocessor.api.service.CustomerService;
import com.aalechnovic.customerdetailsprocessor.api.web.error.CustomerGetPathIdException;
import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.aalechnovic.customerdetailsprocessor.api.web.CustomerController.API_V_1_CUSTOMERS_PATH;

@RestController
@RequestMapping(API_V_1_CUSTOMERS_PATH)
@Validated
public class CustomerController {
    static final String API_V_1_CUSTOMERS_PATH = "/v1/customers";

    private final CustomerService customerService;
    private final PortProvider portProvider;

    public CustomerController(CustomerService customerService, PortProvider portProvider) {
        this.customerService = customerService;
        this.portProvider = portProvider;
    }

    @GetMapping("/{ref}")
    public Mono<ResponseEntity<CusDetails>> findCustomerById(@PathVariable String ref) {
        return Mono.just(ref)
                   .doOnNext(this::validateRef)
                   .flatMap(customerService::findByRef)
                   .map(CustomerController::convertToDomainFromCustomer)
                   .map(cusDetails -> ResponseEntity.ok().body(cusDetails))
                   .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> createCustomer(@RequestBody @Valid CusDetails cusDetails) {
        return customerService.save(convertToCustomerFromDomain(cusDetails))
                              .map(Customer::getCustomerId)
                              .map(cusId -> ResponseEntity.created(getNewRssLocation(cusId)).build());
    }

    private URI getNewRssLocation(String cusId) {
        return URI.create("localhost:" + portProvider.getServerPort() + API_V_1_CUSTOMERS_PATH + "/" + cusId);
    }

    private static Customer convertToCustomerFromDomain(CusDetails cusDetails){
        return new Customer(cusDetails.customerRef(), cusDetails.customerName(), cusDetails.addressLineOne(),
                            cusDetails.addressLineTwo(), cusDetails.town(), cusDetails.county(), cusDetails.country(),
                            cusDetails.postCode(), true);
    }

    private static CusDetails convertToDomainFromCustomer(Customer customer){
        return new CusDetails(customer.getCustomerId(), customer.getName(), customer.getAddressLineOne(),
                              customer.getAddressLineTwo(), customer.getTown(), customer.getCounty(), customer.getCountry(),
                              customer.getPostCode());
    }

    private void validateRef(String cusDetailsRef) {
        if (cusDetailsRef.length() != CusDetails.CUS_DETAILS_REF_ALLOWED)
            throw new CustomerGetPathIdException();
    }
}
