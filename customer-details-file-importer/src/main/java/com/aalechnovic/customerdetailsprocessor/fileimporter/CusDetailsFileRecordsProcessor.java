package com.aalechnovic.customerdetailsprocessor.fileimporter;

import com.aalechnovic.customerdetailsprocessor.fileimporter.api.CusDetailsApiClient;
import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper.CSVInputFileHeader;
import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper.CSVCusDetailsMetaTransformer;
import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CSVFileRecordsReader;
import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CSVFileRecordsWriter;
import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.CusDetailsMeta;
import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper.CSVReportOutputFileHeader;
import com.aalechnovic.customerdetailsprocessor.fileimporter.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;

public class CusDetailsFileRecordsProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CusDetailsApiClient cusDetailsApiClient;
    private final Validator validator;

    public CusDetailsFileRecordsProcessor(CusDetailsApiClient cusDetailsApiClient, Validator validator) {
        this.cusDetailsApiClient = cusDetailsApiClient;
        this.validator = validator;
    }

    public Flux<CusDetailsMeta> processFileRecords(CSVFileRecordsReader csvFileRecordsReader, CSVFileRecordsWriter csvFileRecordsWriter) {

        Flux<CusDetailsMeta> processRecsFlux = csvFileRecordsReader.readRecords(CSVInputFileHeader.HEADER_NAMES)
                                                                   .map(CSVCusDetailsMetaTransformer::toCusDetailsMeta)
                                                                   .flatMapSequential(this::validateAndSave)
                                                                   .publishOn(Schedulers.single())
                                                                   .concatMap(cusDetailsMeta -> writeOutcome(csvFileRecordsWriter, cusDetailsMeta));

        return Mono.defer(() -> csvFileRecordsWriter.write(CSVReportOutputFileHeader.HEADER_NAMES)).thenMany(processRecsFlux);
    }

    private Mono<CusDetailsMeta> writeOutcome(CSVFileRecordsWriter csvFileRecordsWriter, CusDetailsMeta cusDetailsMeta) {
        String[] res = CSVCusDetailsMetaTransformer.toStringList(cusDetailsMeta);
        return csvFileRecordsWriter.write(res)
                                   .thenReturn(cusDetailsMeta)
                                   .doOnNext(unused -> logger.info("Written result {}", Arrays.toString(res)));
    }


    private Mono<CusDetailsMeta> validateAndSave(CusDetailsMeta cusDetailsMeta) {
        return Mono.just(cusDetailsMeta)
                   .doOnNext(cdm -> validator.validate(cusDetailsMeta.getCustomer()))
                   .flatMap(cdm -> cusDetailsApiClient.postCustomerDetails(cusDetailsMeta.getCustomer())
                                                      .map(cusId -> cusDetailsMeta))
                   .onErrorResume(throwable -> returnMetaWithErrMsg(cusDetailsMeta, throwable));
    }

    private static Mono<CusDetailsMeta> returnMetaWithErrMsg(CusDetailsMeta cusDetailsMeta, Throwable throwable) {
        return Mono.just(CusDetailsMeta.builderFrom(cusDetailsMeta).withFailureReason(throwable.getMessage()).build());
    }
}
