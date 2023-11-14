package com.aalechnovic.customerdetailsprocessor.api;

import com.aalechnovic.customerdetailsprocessor.api.persistence.MySQLTestContainerInitializer;
import com.aalechnovic.customerdetailsprocessor.api.web.PortProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {MySQLTestContainerInitializer.class})
public class IntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @MockBean
    PortProvider provider; // need to use as LocalServerPort isn't resolved during dependencies' injection stage

    @Test
    public void saveNewCustomerAndRetrieve() throws IOException {

        doReturn(port).when(provider).getServerPort();

        String uuid = "6c84fb90-12c4-11e1-840d-7b25c5ee775a";

        String body = Files.readString(Path.of("src/test/resources/postReq.json"));

        webTestClient.post().uri("/v1/customers").contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(body))
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatusCode.valueOf(201))
                     .expectHeader().valueEquals("Location", "localhost:" + port + "/v1/customers/" + uuid);

        webTestClient.get().uri("/v1/customers/"+uuid)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatusCode.valueOf(200))
                     .expectBody().json(body);
    }
}
