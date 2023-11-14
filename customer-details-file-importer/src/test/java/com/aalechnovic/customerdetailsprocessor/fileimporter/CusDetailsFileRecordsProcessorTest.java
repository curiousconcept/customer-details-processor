package com.aalechnovic.customerdetailsprocessor.fileimporter;

import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import com.aalechnovic.customerdetailsprocessor.fileimporter.api.CusDetailsApiClient;
import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CSVFileRecordsReader;
import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CSVFileRecordsWriter;
import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.CusDetailsMeta;
import com.aalechnovic.customerdetailsprocessor.fileimporter.util.Pair;
import com.aalechnovic.customerdetailsprocessor.fileimporter.validation.Validator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper.CSVInputFileHeader.CUSTOMER_REF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CusDetailsFileRecordsProcessorTest {

    @Mock
    private CSVFileRecordsReader csvFileRecordsReader;

    @Mock
    private CSVFileRecordsWriter csvFileRecordsWriter;

    @Mock
    private CusDetailsApiClient cusDetailsApiClient;

    @Mock
    private Validator validator;


    @InjectMocks
    private CusDetailsFileRecordsProcessor cusDetailsFileRecordsProcessor;


    @Test
    public void checkFileRecordsAreTransformedAndSentToApi_metaForEachIsGenerated(){

        String cus_ref_one = "CUS666";
        String cus_ref_two = "CUS999";

        Flux<Pair<Long,Map<String,String>>> fluxRecords = Flux.fromIterable(List.of(Pair.of(5L, Map.of(CUSTOMER_REF.getValue(), cus_ref_one)),
                                                                                    Pair.of(7L, Map.of(CUSTOMER_REF.getValue(), cus_ref_two))));

        doReturn(fluxRecords).when(csvFileRecordsReader).readRecords(any());
        doReturn(Mono.empty()).when(csvFileRecordsWriter).write(any());

        doReturn(Mono.just(cus_ref_one)).when(cusDetailsApiClient).postCustomerDetails(genCusWithRef(cus_ref_one));
        doReturn(Mono.just(cus_ref_two)).when(cusDetailsApiClient).postCustomerDetails(genCusWithRef(cus_ref_two));

        List<CusDetailsMeta> outputFilePath = cusDetailsFileRecordsProcessor.processFileRecords(csvFileRecordsReader, csvFileRecordsWriter)
                                                                            .collectList().block();

        assertThat(outputFilePath).hasSize(2);
        assertThat(outputFilePath).contains(new CusDetailsMeta.Builder().withCustomer(genCusWithRef(cus_ref_one)).withFileRecordId(5L).build(),
                                            new CusDetailsMeta.Builder().withCustomer(genCusWithRef(cus_ref_two)).withFileRecordId(7L).build());
    }

    @Test
    public void checkValidationBreakingRecord_DoesNotBreakTheOther_RecordsCorrectOutput(){

        String cus_ref_one = "CUS666";
        String cus_ref_two = "CUS999";

        Flux<Pair<Long,Map<String,String>>> fluxRecords = Flux.fromIterable(List.of(Pair.of(5L, Map.of(CUSTOMER_REF.getValue(), cus_ref_one)),
                                                                                    Pair.of(7L, Map.of(CUSTOMER_REF.getValue(), cus_ref_two))));

        doReturn(fluxRecords).when(csvFileRecordsReader).readRecords(any());
        doReturn(Mono.empty()).when(csvFileRecordsWriter).write(any());

        doReturn(Mono.just(cus_ref_one)).when(cusDetailsApiClient).postCustomerDetails(genCusWithRef(cus_ref_one));

        doAnswer(invocation -> {
            CusDetails arg = invocation.getArgument(0);
            if (arg.customerRef().equals("CUS999")) {
                throw new ValidationException("Validation");
            }
            return null;
        }).when(validator).validate(any(CusDetails.class));

        List<CusDetailsMeta> output = cusDetailsFileRecordsProcessor.processFileRecords(csvFileRecordsReader, csvFileRecordsWriter)
                                                                            .collectList().block();

        assertThat(output).hasSize(2);
        assertThat(output).contains(new CusDetailsMeta.Builder().withCustomer(genCusWithRef(cus_ref_one))
                                                                        .withFileRecordId(5L)
                                                                        .build(),
                                            new CusDetailsMeta.Builder().withCustomer(genCusWithRef(cus_ref_two))
                                                                        .withFileRecordId(7L)
                                                                        .withFailureReason("Validation")
                                                                        .build());
    }



    private static CusDetails genCusWithRef(String customerRef){
        return new CusDetails(customerRef, null, null, null, null, null, null, null);
    }
}
