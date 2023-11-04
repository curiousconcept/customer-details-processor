package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Map;

public interface FileRecordsExtractor {

    Flux<Map<String,String>> extractFluxRecordsFrom(InputStream inputStream);
}
