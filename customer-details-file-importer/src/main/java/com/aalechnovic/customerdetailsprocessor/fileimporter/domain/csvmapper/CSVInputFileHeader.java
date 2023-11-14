package com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper;

import java.util.Arrays;

public enum CSVInputFileHeader {
    CUSTOMER_REF("Customer Ref"),
    CUSTOMER_NAME("Customer Name"),
    ADDRESS_LINE_1("Address Line 1"),
    ADDRESS_LINE_2("Address Line 2"),
    TOWN("Town"),
    COUNTY("County"),
    COUNTRY("Country"),
    POSTCODE("Postcode");

    public static final String[] HEADER_NAMES = Arrays.stream(CSVInputFileHeader.values())
                                                      .map(csvCusDetailsInputFileHeader -> csvCusDetailsInputFileHeader.value)
                                                      .toArray(String[]::new);

    private final String value;

    CSVInputFileHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
