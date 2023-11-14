package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CSVFileRecordsWriter {
    Mono<Void> write(String[] lineValues);
}
