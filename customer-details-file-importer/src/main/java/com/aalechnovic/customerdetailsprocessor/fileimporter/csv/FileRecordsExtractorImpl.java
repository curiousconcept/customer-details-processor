package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FileRecordsExtractorImpl implements FileRecordsExtractor {


    public static final String[] HEADER_NAMES = {"Customer Ref", "Customer Name", "Address Line 1", "Address Line 2",
                                                 "Town", "County", "Country", "Postcode"};

    @Override
    public Flux<Map<String, String>> extractFluxRecordsFrom(InputStream inputStream) {

        BufferedReader buffered =  new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        try {
            var csvParser = CSVFormat.RFC4180.builder().setHeader(HEADER_NAMES).setSkipHeaderRecord(true).build()
                                             .parse(buffered);

            return Flux.fromIterable(csvParser).map(CSVRecord::toMap);
        } catch (IOException ioException){
            throw new UncheckedIOException(ioException);
        }
    }
}
