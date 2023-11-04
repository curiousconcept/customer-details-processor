package com.aalechnovic.customerdetailsprocessor.fileimporter;

import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.FileRecordsExtractor;
import com.aalechnovic.customerdetailsprocessor.fileimporter.fs.FileLoader;
import reactor.core.publisher.Flux;

import java.io.InputStream;

public class FileImporterService {

    private final FileLoader fileLoadStrategy;
    private final FileRecordsExtractor fileRecordsExtractor;

    public FileImporterService(FileLoader fileLoadStrategy) {
        this.fileLoadStrategy = fileLoadStrategy;
    }

    public Flux<ImportResult> importFile(String path){

        InputStream inputStream = fileLoadStrategy.readFileAsInputStream(path);
        fileRecordsExtractor.extractFluxRecordsFrom(inputStream);
    }
}
