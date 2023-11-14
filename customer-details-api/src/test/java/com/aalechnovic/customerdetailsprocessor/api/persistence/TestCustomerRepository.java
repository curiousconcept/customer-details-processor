package com.aalechnovic.customerdetailsprocessor.api.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcWithContainerTest
public class TestCustomerRepository {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void test(){
        customerRepository.save(new Customer("bla","bla","bla","bla","bla","bla","bla","bla", true)).block();

        Customer expectedCustomer = new Customer("bla","bla","bla","bla","bla","bla","bla","bla");
        Customer dbCustomer = customerRepository.findById("bla").block();

        assertThat(dbCustomer).isNotNull();
        assertThat(dbCustomer).isEqualTo(expectedCustomer);
        assertThat(dbCustomer.isNew()).isFalse();

    }
}
