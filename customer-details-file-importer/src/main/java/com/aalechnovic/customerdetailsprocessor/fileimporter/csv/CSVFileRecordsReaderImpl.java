package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import com.aalechnovic.customerdetailsprocessor.fileimporter.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.util.Map;

public class CSVFileRecordsReaderImpl implements CSVFileRecordsReader {

    private final Reader reader;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CSVFileRecordsReaderImpl(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Flux<Pair<Long, Map<String, String>>> readRecords(String[] headerValues) {
        try {

            CSVParser csvParser = CSVFormat.RFC4180.builder()
                                                   .setIgnoreEmptyLines(true)
                                                   .setSkipHeaderRecord(true)
                                                   .setHeader(headerValues)
                                                   .build()
                                                   .parse(reader);

            return Flux.fromIterable(csvParser).map(csvRecord -> Pair.of(csvRecord.getRecordNumber(), csvRecord.toMap()))
                       .doOnNext(pair -> logger.info("Extracted CSV record {}", pair.getSecond())).subscribeOn(Schedulers.single());
        } catch (IOException ioException) {
            throw new UncheckedIOException(ioException);
        }
    }
}
