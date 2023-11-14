package com.aalechnovic.customerdetailsprocessor.api.service;

import com.aalechnovic.customerdetailsprocessor.api.persistence.Customer;
import com.aalechnovic.customerdetailsprocessor.api.persistence.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testSaveNewCustomer(){
        Customer testCustomer = new Customer("6c84fb90-12c4-11e1-840d-7b25c5ee775a","","","","","","","",true);

        when(customerRepository.findById("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).thenReturn(Mono.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer((Answer<Mono<Customer>>) invocation -> Mono.just(invocation.getArgument(0)));

        Customer expectedCustomer = new Customer("6c84fb90-12c4-11e1-840d-7b25c5ee775a","","","","","","","",true);

        assertThat(customerService.save(testCustomer).block()).isEqualTo(expectedCustomer);
    }

    @Test
    public void testSaveExistingCustomer_expectError(){
        String uuid = "6c84fb90-12c4-11e1-840d-7b25c5ee775a";

        Customer testCustomer = new Customer(uuid, "", "", "", "", "", "", "", true);

        when(customerRepository.findById(uuid)).thenReturn(Mono.just(testCustomer));


        StepVerifier.create(customerService.save(testCustomer))
                    .expectErrorSatisfies(throwable -> {assertThat(throwable).isInstanceOf(CustomerAlreadyExistsException.class);
                        assertThat(throwable).hasMessage(uuid);
                    }).verify();


    }

}