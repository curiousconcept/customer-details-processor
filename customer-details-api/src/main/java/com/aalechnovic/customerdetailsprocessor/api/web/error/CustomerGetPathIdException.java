package com.aalechnovic.customerdetailsprocessor.api.web.error;

import com.aalechnovic.customerdetailsprocessor.domain.CusDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.aalechnovic.customerdetailsprocessor.api.web.error.CustomerGetPathIdException.REASON;

// TODO figure out why response reason isn't returned
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = REASON)
public class CustomerGetPathIdException extends RuntimeException{

    public static final String REASON = "Customer ref must be exactly: " + CusDetails.CUS_DETAILS_REF_ALLOWED + " characters";
}
