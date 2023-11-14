package com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper;

import java.util.Arrays;

public enum CSVReportOutputFileHeader {
    INPUT_FILE_LINE("Input File Line"),
    CUSTOMER_REF(CSVInputFileHeader.CUSTOMER_REF.getValue()),
    STATUS("Status");

    public static final String[] HEADER_NAMES = Arrays.stream(CSVReportOutputFileHeader.values())
                                                      .map(csvCusDetailsInputFileHeader -> csvCusDetailsInputFileHeader.value)
                                                      .toArray(String[]::new);
    private final String value;

    CSVReportOutputFileHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
