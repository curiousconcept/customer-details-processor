package com.aalechnovic.customerdetailsprocessor.api.persistence;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

public class Customer implements Persistable<String> {
//    CREATE TABLE IF NOT EXISTS `customer` (
//            `customer_id` varchar(32) NOT NULL,
//  `name` varchar(255) NOT NULL,
//  `address_line_one` varchar(150) NOT NULL,
//  `address_line_two` varchar(150),
//  `town` varchar(60) NOT NULL,
//  `county` varchar(50),
//  `country` varchar(60) NOT NULL,
//  `post_code` varchar(50) NOT NULL,
//    PRIMARY KEY (`customer_id`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    @Id // needed for deserialization from db record, despite the fact we have Persistable interface
    private final String customerId;

    @Size(min = 1 , max = 255)
    private final String name;
    @Size(min = 1 , max = 150)
    private final String addressLineOne;
    @Size(max = 150)
    private final String addressLineTwo;
    @Size(min = 1, max = 60)
    private final String town;
    @Size(max = 50)
    private final String county;
    @Size(min = 1, max = 60)
    private final String country;
    @Size(min = 1, max = 50)
    private final String postCode;

    @Transient
    private final boolean isNew; // allows us to insert with id (spring r2dbc tries to update if id is provided)

    @PersistenceCreator
    Customer(String customerId, String name, String addressLineOne, String addressLineTwo, String town, String county, String country, String postCode) {
        this(customerId, name, addressLineOne, addressLineTwo, town, county, country, postCode, false);
    }

    public Customer(String customerId,
                    String name,
                    String addressLineOne,
                    String addressLineTwo,
                    String town,
                    String county,
                    String country,
                    String postCode,
                    boolean isNew) {

        this.customerId = customerId;
        this.name = name;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.town = town;
        this.county = county;
        this.country = country;
        this.postCode = postCode;
        this.isNew = isNew;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) && Objects.equals(name, customer.name) && Objects.equals(addressLineOne, customer.addressLineOne) && Objects.equals(addressLineTwo, customer.addressLineTwo) && Objects.equals(town, customer.town) && Objects.equals(county, customer.county) && Objects.equals(country, customer.country) && Objects.equals(postCode, customer.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, addressLineOne, addressLineTwo, town, county, country, postCode);
    }

    @Override
    public String toString() {
        return "Customer{" +
               "customerRef='" + customerId + '\'' +
               ", customerName='" + name + '\'' +
               ", addressLineOne='" + addressLineOne + '\'' +
               ", addressLineTwo='" + addressLineTwo + '\'' +
               ", town='" + town + '\'' +
               ", county='" + county + '\'' +
               ", country='" + country + '\'' +
               ", postCode='" + postCode + '\'' +
               '}';
    }

    @Override
    public String getId() {
        return this.customerId;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
