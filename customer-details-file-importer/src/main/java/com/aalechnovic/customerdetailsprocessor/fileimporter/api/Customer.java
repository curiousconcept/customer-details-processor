package com.aalechnovic.customerdetailsprocessor.fileimporter.api;


import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CustomerRecord;

import java.util.Map;
import java.util.Objects;

public class Customer {

    private final String customerRef;
    private final String customerName;
    private final String addressLine1;
    private final String addressLine2;
    private final String town;
    private final String county;
    private final String country;
    private final String postCode;

    public Customer(String customerRef, String customerName, String addressLine1, String addressLine2, String town,
                    String county, String country, String postCode) {
        this.customerRef = customerRef;
        this.customerName = customerName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.town = town;
        this.county = county;
        this.country = country;
        this.postCode = postCode;
    }

    public String getCustomerRef() {
        return customerRef;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getTown() {
        return town;
    }

    public String getCounty() {
        return county;
    }

    public String getCountry() {
        return country;
    }

    public String getPostCode() {
        return postCode;
    }

    @Override
    public String toString() {
        return "Customer{" +
               "customerRef='" + customerRef + '\'' +
               ", customerName='" + customerName + '\'' +
               ", addressLine1='" + addressLine1 + '\'' +
               ", addressLine2='" + addressLine2 + '\'' +
               ", town='" + town + '\'' +
               ", county='" + county + '\'' +
               ", country='" + country + '\'' +
               ", postCode='" + postCode + '\'' +
               '}';
    }

    public static Customer from(Map<String, String> record){
        return new Customer(record.get(CustomerRecord.CUSTOMER_REF.getValue()) ,
                            record.get(CustomerRecord.CUSTOMER_NAME.getValue()) ,
                            record.get(CustomerRecord.ADDRESS_LINE_1.getValue()),
                            record.get(CustomerRecord.ADDRESS_LINE_2.getValue()),
                            record.get(CustomerRecord.TOWN.getValue()),
                            record.get(CustomerRecord.COUNTY.getValue()),
                            record.get(CustomerRecord.COUNTRY.getValue()),
                            record.get(CustomerRecord.POSTCODE.getValue()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerRef, customer.customerRef) && Objects.equals(customerName, customer.customerName)
               && Objects.equals(addressLine1, customer.addressLine1) && Objects.equals(addressLine2, customer.addressLine2)
               && Objects.equals(town, customer.town) && Objects.equals(county, customer.county) &&
               Objects.equals(country, customer.country) && Objects.equals(postCode, customer.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerRef, customerName, addressLine1, addressLine2, town, county, country, postCode);
    }
}
