package com.aalechnovic.customerdetailsprocessor.fileimporter.api;

import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import com.aalechnovic.customerdetailsprocessor.fileimporter.api.util.WireMockConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@WebFluxTest
@ContextConfiguration(classes = CusDetailsApiClientConfig.class)
@Import({WireMockConfig.class})
class CusDetailsApiClientTest {

    @Autowired
    private CusDetailsApiClient cusDetailsApiClient;

    @Autowired
    private WireMockServer wireMockServer;

    @Test
    void sendCustomerDetailsToApi_OK(){

        CusDetails testCusDetails = new CusDetails("de29fb90-12c4-11e1-840d-7b25c5ee775a", "John Doe", "99 Cranberry Avenue", "Strawberry Prospect",
                                                   "Sunny Town", "Perfect County", "Neverland", "NL2 20GS");

        StepVerifier.create(cusDetailsApiClient.postCustomerDetails(testCusDetails))
                    .expectNext(String.format("localhost:%s/v1/customers/de29fb90-12c4-11e1-840d-7b25c5ee775a", wireMockServer.port())).verifyComplete();
    }


    // TODO each case needs to be handled uniquely rather than all-in-one scenario
    @Test
    void sendCustomerDetailsToApiExternalFaults_retryRecovers() {
        CusDetails testCusDetails = new CusDetails("FAULT666-12c4-11e1-840d-7b25c5ee775a", "Whatever", "Whatever", "Whatever",
                                                   "Whatever", "Whatever", "Whatever", "Whatever");
        StepVerifier.create(cusDetailsApiClient.postCustomerDetails(testCusDetails))
                    .expectNext(String.format("localhost:%s/v1/customers/FAULT666-12c4-11e1-840d-7b25c5ee775a", wireMockServer.port())).verifyComplete();
    }

}