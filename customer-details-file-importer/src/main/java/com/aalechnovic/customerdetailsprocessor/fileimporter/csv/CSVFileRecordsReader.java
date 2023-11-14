package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import com.aalechnovic.customerdetailsprocessor.fileimporter.util.Pair;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface CSVFileRecordsReader {

    Flux<Pair<Long, Map<String, String>>> readRecords(String[] headerValues);
}
