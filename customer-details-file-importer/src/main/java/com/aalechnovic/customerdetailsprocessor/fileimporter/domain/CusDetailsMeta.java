package com.aalechnovic.customerdetailsprocessor.fileimporter.domain;

import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;

import java.util.Objects;
import java.util.Optional;

/**
 * Wrapper class for CusDetails including details such as original file record line(id) and failure reason(if applicable)
 * as well as status denoting success.
 */
public class CusDetailsMeta {
    private final Long fileRecordId;
    private final CusDetails cusDetails;
    private final String failureReason;

    private CusDetailsMeta(Long fileRecordId, CusDetails cusDetails, String failureReason) {
        this.fileRecordId = fileRecordId;
        this.cusDetails = cusDetails;
        this.failureReason = failureReason;
    }

    public Long getFileRecordId() {
        return fileRecordId;
    }


    public Optional<String> getFailureReason() {
        return Optional.ofNullable(failureReason);
    }

    public CusDetails getCustomer() {
        return cusDetails;
    }

    @Override
    public String toString() {
        return "CustomerMeta{" +
               "fileRecordId=" + fileRecordId +
               ", failureReason='" + failureReason + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CusDetailsMeta that = (CusDetailsMeta) o;
        return Objects.equals(fileRecordId, that.fileRecordId) && Objects.equals(cusDetails, that.cusDetails)
               && Objects.equals(failureReason, that.failureReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileRecordId, cusDetails, failureReason);
    }

    public static Builder builderFrom(CusDetailsMeta cusDetailsMeta){
        return new Builder().withCustomer(cusDetailsMeta.cusDetails)
                            .withFileRecordId(cusDetailsMeta.fileRecordId)
                            .withFailureReason(cusDetailsMeta.failureReason);
    }

    public static class Builder {
        private Long fileRecordId;
        private CusDetails cusDetails;
        private String failureReason;

        public Builder withFileRecordId(Long fileRecordId) {
            this.fileRecordId = fileRecordId;
            return this;
        }

        public Builder withCustomer(CusDetails cusDetails) {
            this.cusDetails = cusDetails;
            return this;
        }

        public Builder withFailureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public CusDetailsMeta build() {
            return new CusDetailsMeta(fileRecordId, cusDetails, failureReason);
        }
    }
}
