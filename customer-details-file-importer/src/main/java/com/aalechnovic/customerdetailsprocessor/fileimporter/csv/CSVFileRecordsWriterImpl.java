package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class CSVFileRecordsWriterImpl implements CSVFileRecordsWriter {

    private final CSVPrinter printer;

    public CSVFileRecordsWriterImpl(Writer writer) {
        try {
            this.printer = new CSVPrinter(writer, CSVFormat.RFC4180);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Void> write(String[] lineValues) {
        return Mono.defer(() -> {
                              try {
                                  printer.printRecord(Arrays.asList(lineValues));
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }

                              return Mono.empty();
                          });
    }
}
