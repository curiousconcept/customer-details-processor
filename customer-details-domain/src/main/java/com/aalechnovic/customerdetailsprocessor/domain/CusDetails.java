package com.aalechnovic.customerdetailsprocessor.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CusDetails(@NotNull @Size(min = CUS_DETAILS_REF_ALLOWED, max = CUS_DETAILS_REF_ALLOWED) String customerRef, @NotNull @Size(min = 1, max = 255) String customerName,
                         @NotNull @Size(min = 1, max = 150) String addressLineOne, @Size(max = 150) String addressLineTwo,
                         @NotNull @Size(min = 1, max = 60) String town, @Size(max = 50) String county,
                         @NotNull @Size(min = 1, max = 60) String country, @NotNull @Size(min = 1, max = 50) String postCode) {

    public static final int CUS_DETAILS_REF_ALLOWED = 36;

    public CusDetails(String customerRef, String customerName, String addressLineOne, String addressLineTwo,
                      String town, String county, String country, String postCode) {
        this.customerRef = customerRef;
        this.customerName = customerName;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.town = town;
        this.county = county;
        this.country = country;
        this.postCode = postCode;
    }

    @Override
    public String customerRef() {
        return customerRef;
    }

    @Override
    public String customerName() {
        return customerName;
    }

    @Override
    public String addressLineOne() {
        return addressLineOne;
    }

    @Override
    public String addressLineTwo() {
        return addressLineTwo;
    }

    @Override
    public String town() {
        return town;
    }

    @Override
    public String county() {
        return county;
    }

    @Override
    public String country() {
        return country;
    }

    @Override
    public String postCode() {
        return postCode;
    }

    @Override
    public String toString() {
        return "CusDetails{" +
               "customerRef='" + customerRef + '\'' +
               ", customerName='" + customerName + '\'' +
               ", addressLineOne='" + addressLineOne + '\'' +
               ", addressLineTwo='" + addressLineTwo + '\'' +
               ", town='" + town + '\'' +
               ", county='" + county + '\'' +
               ", country='" + country + '\'' +
               ", postCode='" + postCode + '\'' +
               '}';
    }
}