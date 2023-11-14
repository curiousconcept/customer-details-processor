package com.aalechnovic.customerdetailsprocessor.fileimporter.domain.csvmapper;

import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import com.aalechnovic.customerdetailsprocessor.fileimporter.domain.CusDetailsMeta;
import com.aalechnovic.customerdetailsprocessor.fileimporter.util.Pair;

import java.util.Map;

public class CSVCusDetailsMetaTransformer {

    public static String[] toStringList(CusDetailsMeta cusDetailsMeta){
            String status = cusDetailsMeta.getFailureReason().orElse("success");
            String fileRecId = String.valueOf(cusDetailsMeta.getFileRecordId());
            return new String[]{fileRecId, cusDetailsMeta.getCustomer().customerRef(), status};

    }

    public static CusDetailsMeta toCusDetailsMeta(Pair<Long, Map<String, String>> idAndRecord){
        return new CusDetailsMeta.Builder().withFileRecordId(idAndRecord.getFirst())
                                           .withCustomer(from(idAndRecord.getSecond()))
                                           .build();

    }

    private static CusDetails from(Map<String, String> inputRecord) {
        return new CusDetails(inputRecord.get(CSVInputFileHeader.CUSTOMER_REF.getValue()),
                              inputRecord.get(CSVInputFileHeader.CUSTOMER_NAME.getValue()),
                              inputRecord.get(CSVInputFileHeader.ADDRESS_LINE_1.getValue()),
                              inputRecord.get(CSVInputFileHeader.ADDRESS_LINE_2.getValue()),
                              inputRecord.get(CSVInputFileHeader.TOWN.getValue()),
                              inputRecord.get(CSVInputFileHeader.COUNTY.getValue()),
                              inputRecord.get(CSVInputFileHeader.COUNTRY.getValue()),
                              inputRecord.get(CSVInputFileHeader.POSTCODE.getValue()));
    }

}
