package com.aalechnovic.customerdetailsprocessor.fileimporter.api;


import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetryBackoffSpec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CusDetailsApiClient {

    public static final Duration RETRY_START_DELAY = Duration.of(1, ChronoUnit.SECONDS);
    public static final int RETRY_MAX_ATTEMPTS = 5;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WebClient webClient;

    public CusDetailsApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> postCustomerDetails(CusDetails cusDetails) {

       return this.webClient.post()
                            .body(BodyInserters.fromValue(cusDetails))
                            .accept(MediaType.APPLICATION_JSON)
                            .acceptCharset(StandardCharsets.UTF_8)
                            .retrieve().toBodilessEntity()
                            .map(voidResponseEntity -> getLocation(voidResponseEntity).orElseThrow(() -> new IllegalStateException("Location header not found in the response")) )
                            .doOnNext(id -> logger.info("Created new API record {}", id))
                            .retryWhen(RetryBackoffSpec.backoff(RETRY_MAX_ATTEMPTS, RETRY_START_DELAY)
                                                       .filter(this::isRetryException));
    }

    private boolean isRetryException(Throwable throwable) {
        return (throwable instanceof WebClientResponseException &&
               ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()) ||
               (throwable instanceof WebClientRequestException && throwable.getMessage().contains("Connection reset")) ;
    }

    private Optional<String> getLocation(ResponseEntity<Void> responseEntity){
        HttpHeaders headers = responseEntity.getHeaders();
        if (headers.containsKey(HttpHeaders.LOCATION)) {
            String location = headers.getFirst(HttpHeaders.LOCATION);
            return Optional.ofNullable(location);
        } else {
            return Optional.empty();
        }
    }
}
