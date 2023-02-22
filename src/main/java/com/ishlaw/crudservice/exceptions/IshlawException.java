package com.ishlaw.crudservice.exceptions;



import com.ishlaw.crudservice.Utils.IshlawConstants;
import org.springframework.http.HttpStatus;

public class IshlawException extends Exception {
    IshlawConstants.ApiResponseCodes responseCode;
    final String responseMessage;
    final HttpStatus httpStatus;

    public IshlawException(String message) {
        super(message);
        this.responseCode = IshlawConstants.ApiResponseCodes.GENERAL_ERROR;
        this.responseMessage = message;
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
    }

    public IshlawException(String message, IshlawConstants.ApiResponseCodes responseCode) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = message;
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
    }

    public IshlawException(String message, IshlawConstants.ApiResponseCodes responseCode, String responseMessage) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
    }

    public IshlawException(String message, IshlawConstants.ApiResponseCodes responseCode, String responseMessage, HttpStatus httpStatus) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }

    public IshlawConstants.ApiResponseCodes getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
