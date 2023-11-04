package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FileRecordsExtractorImplTest {

    private final FileRecordsExtractor fileRecordsExtractor = new FileRecordsExtractorImpl();


    @Test
    void checkMapContainsRecord_ok() throws IOException {
        String test = """
                      Customer Ref, Customer Name, Address Line 1, Address Line 2, Town, County, Country, Postcode
                      DE29,John Doe,99 Cranberry Avenue,Strawberry Prospect,Sunny Town,Perfect County,Neverland,NL2 20GS
                      FL20,Amy Wang,2 Baker Street,,Shady Town,Impeccable County,Wonderland,WO9 591
                      """;
        StepVerifier.create(fileRecordsExtractor.extractFluxRecordsFrom(new ByteArrayInputStream(test.getBytes())))
                    .consumeNextWith(stringStringMap -> {
                        assertThat(stringStringMap).isNotNull().isNotEmpty();
                        assertThat(stringStringMap.get("Customer Ref")).isEqualTo("DE29");
                        assertThat(stringStringMap.get("Customer Name")).isEqualTo("John Doe");
                        assertThat(stringStringMap.get("Address Line 1")).isEqualTo("99 Cranberry Avenue");
                        assertThat(stringStringMap.get("Address Line 2")).isEqualTo("Strawberry Prospect");
                        assertThat(stringStringMap.get("Town")).isEqualTo("Sunny Town");
                        assertThat(stringStringMap.get("County")).isEqualTo("Perfect County");
                        assertThat(stringStringMap.get("Country")).isEqualTo("Neverland");
                        assertThat(stringStringMap.get("Postcode")).isEqualTo("NL2 20GS");
                    })
                    .consumeNextWith(stringStringMap -> {
                        assertThat(stringStringMap).isNotNull().isNotEmpty();
                        assertThat(stringStringMap.get("Customer Ref")).isEqualTo("FL20");
                        assertThat(stringStringMap.get("Customer Name")).isEqualTo("Amy Wang");
                        assertThat(stringStringMap.get("Address Line 1")).isEqualTo("2 Baker Street");
                        assertThat(stringStringMap.get("Address Line 2")).isEqualTo("");
                        assertThat(stringStringMap.get("Town")).isEqualTo("Shady Town");
                        assertThat(stringStringMap.get("County")).isEqualTo("Impeccable County");
                        assertThat(stringStringMap.get("Country")).isEqualTo("Wonderland");
                        assertThat(stringStringMap.get("Postcode")).isEqualTo("WO9 591");
                    })
                    .verifyComplete();
    }

    @Test
    void testZeroBytes_exitsOK() throws IOException {
        StepVerifier.create(fileRecordsExtractor.extractFluxRecordsFrom(new ByteArrayInputStream(new byte[0])))
                    .verifyComplete();
    }


}