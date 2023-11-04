package com.aalechnovic.customerdetailsprocessor.fileimporter.csv;

public enum CustomerRecord {

    CUSTOMER_REF("Customer Ref"),
    CUSTOMER_NAME("Customer Name"),
    ADDRESS_LINE_1("Address Line 1"),
    ADDRESS_LINE_2("Address Line 2"),
    TOWN("Town"),
    COUNTY("County"),
    COUNTRY("Country"),
    POSTCODE("Postcode");

    private final String value;

    CustomerRecord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
