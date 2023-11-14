package com.aalechnovic.customerdetailsprocessor.fileimporter.api.util;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
public class WireMockConfig {
    @Bean
    public WireMockServer webServer() {
        WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        return wireMockServer;
    }

    @Bean
    public WebClient webClient(WireMockServer server, @Value("${customer-details-api.path}") String path) {
        return WebClient.builder().baseUrl(server.baseUrl()+path).build();
    }
}

