package com.aalechnovic.customerdetailsprocessor.api.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
}