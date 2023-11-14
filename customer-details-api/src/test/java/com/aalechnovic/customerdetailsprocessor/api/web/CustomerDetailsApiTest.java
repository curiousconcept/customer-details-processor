package com.aalechnovic.customerdetailsprocessor.api.web;

import com.aalechnovic.customerdetailsprocessor.api.persistence.Customer;
import com.aalechnovic.customerdetailsprocessor.api.service.CustomerAlreadyExistsException;
import com.aalechnovic.customerdetailsprocessor.api.service.CustomerService;
import com.aalechnovic.customerdetailsprocessor.api.web.error.CustomerGetPathIdException;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
public class CustomerDetailsApiTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private PortProvider provider;

    @Test
    public void postNewCusDetails() throws IOException {

        when(provider.getServerPort()).thenReturn(8080);

        when(customerService.save(any(Customer.class))).thenAnswer((Answer<Mono<Customer>>) invocation -> Mono.just(invocation.getArgument(0)));

        webClient.post().uri("/v1/customers").contentType(MediaType.APPLICATION_JSON)
                 .body(BodyInserters.fromValue(Files.readString(Path.of("src/test/resources/postReq.json"))))
                 .exchange()
                 .expectStatus().isEqualTo(HttpStatusCode.valueOf(201))
                 .expectHeader().valueEquals("Location", "localhost:8080/v1/customers/6c84fb90-12c4-11e1-840d-7b25c5ee775a");
    }

    @Test
    public void postExistingCusDetails() throws IOException {

        when(provider.getServerPort()).thenReturn(8080);

        when(customerService.save(any(Customer.class))).thenReturn(Mono.error(new CustomerAlreadyExistsException("6c84fb90-12c4-11e1-840d-7b25c5ee775a")));

        webClient.post().uri("/v1/customers").contentType(MediaType.APPLICATION_JSON)
                 .body(BodyInserters.fromValue(Files.readString(Path.of("src/test/resources/postReq.json"))))
                 .exchange()
                 .expectStatus().isEqualTo(HttpStatusCode.valueOf(409))
                 .expectBody().json("{\"customer_ref\":\"6c84fb90-12c4-11e1-840d-7b25c5ee775a\"}");
    }

    @Test
    public void getExistingCusDetails_invalidRef_FAIL()  {

        webClient.get().uri("/v1/customers/abc")
                 .exchange()
                 .expectStatus().isEqualTo(HttpStatusCode.valueOf(400));
//                 .expectStatus().reasonEquals(CustomerGetPathIdException.REASON); TODO figure out why SF refuses to return this
    }

    @Test
    public void getExistingCusDetails_OK(){

        when(customerService.findByRef("de29fb90-12c4-11e1-840d-7b25c5ee775a"))
                .thenReturn(Mono.just(new Customer("6c84fb90-12c4-11e1-840d-7b25c5ee775a",
                                                   "some","some","some","some","some","some","some",false)));


        webClient.get().uri("/v1/customers/de29fb90-12c4-11e1-840d-7b25c5ee775a")
                 .exchange()
                 .expectStatus().isEqualTo(HttpStatusCode.valueOf(200))
                 .expectBody().json("{\"customer_ref\":\"6c84fb90-12c4-11e1-840d-7b25c5ee775a\"," +
                                    "\"customer_name\":\"some\"," +
                                    "\"address_line_one\":\"some\"," +
                                    "\"address_line_two\":\"some\"," +
                                    "\"town\":\"some\"," +
                                    "\"county\":\"some\"," +
                                    "\"country\":\"some\"," +
                                    "\"post_code\":\"some\"}\n");
    }
}
