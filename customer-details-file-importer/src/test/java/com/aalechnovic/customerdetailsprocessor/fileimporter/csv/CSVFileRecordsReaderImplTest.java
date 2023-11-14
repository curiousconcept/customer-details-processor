package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper.CSVInputFileHeader;
import com.aalechnovic.customerdetailsprocessor.fileimporter.util.TestReader;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class CSVFileRecordsReaderImplTest {

    private static final String test = """
                                       Customer Ref, Customer Name, Address Line 1, Address Line 2, Town, County, Country, Postcode
                                       DE29,John Doe,99 Cranberry Avenue,Strawberry Prospect,Sunny Town,Perfect County,Neverland,NL2 20GS
                                       FL20,Amy Wang,2 Baker Street,,Shady Town,Impeccable County,Wonderland,WO9 591
                                       """;

    private final CSVFileRecordsReader fileRecordsExtractor = new CSVFileRecordsReaderImpl(new TestReader(test));


    @Test
    void checkMapContainsRecord_ok() {

        StepVerifier.create(fileRecordsExtractor.readRecords(CSVInputFileHeader.HEADER_NAMES))
                    .consumeNextWith(pair -> {
                        assertThat(pair).isNotNull();
                        assertThat(pair.getFirst()).isEqualTo(1L);
                        assertThat(pair.getSecond().get("Customer Ref")).isEqualTo("DE29");
                        assertThat(pair.getSecond().get("Customer Name")).isEqualTo("John Doe");
                        assertThat(pair.getSecond().get("Address Line 1")).isEqualTo("99 Cranberry Avenue");
                        assertThat(pair.getSecond().get("Address Line 2")).isEqualTo("Strawberry Prospect");
                        assertThat(pair.getSecond().get("Town")).isEqualTo("Sunny Town");
                        assertThat(pair.getSecond().get("County")).isEqualTo("Perfect County");
                        assertThat(pair.getSecond().get("Country")).isEqualTo("Neverland");
                        assertThat(pair.getSecond().get("Postcode")).isEqualTo("NL2 20GS");
                    })
                    .consumeNextWith(stringStringMap -> {
                        assertThat(stringStringMap).isNotNull();
                        assertThat(stringStringMap.getFirst()).isEqualTo(2L);
                        assertThat(stringStringMap.getSecond().get("Customer Ref")).isEqualTo("FL20");
                        assertThat(stringStringMap.getSecond().get("Customer Name")).isEqualTo("Amy Wang");
                        assertThat(stringStringMap.getSecond().get("Address Line 1")).isEqualTo("2 Baker Street");
                        assertThat(stringStringMap.getSecond().get("Address Line 2")).isEqualTo("");
                        assertThat(stringStringMap.getSecond().get("Town")).isEqualTo("Shady Town");
                        assertThat(stringStringMap.getSecond().get("County")).isEqualTo("Impeccable County");
                        assertThat(stringStringMap.getSecond().get("Country")).isEqualTo("Wonderland");
                        assertThat(stringStringMap.getSecond().get("Postcode")).isEqualTo("WO9 591");
                    })
                    .verifyComplete();
    }
}