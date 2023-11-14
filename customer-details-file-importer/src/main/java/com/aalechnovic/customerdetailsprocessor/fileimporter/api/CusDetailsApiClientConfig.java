package com.aalechnovic.customerdetailsprocessor.fileimporter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class CusDetailsApiClientConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public CusDetailsApiClient customerDetailsApiClient(WebClient webClient){
        return new CusDetailsApiClient(webClient);
    }

    @Bean
    public WebClient apiHttpClient(@Value("${customer-details-api.url}") String url,
                                   @Value("${customer-details-api.path}") String path) {

        logger.info("URL is {}", url);
        logger.info("URL Path is {}", path);

        return WebClient.builder().baseUrl(url + path).build();
    }
}
