package com.ishlaw.crudservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {"responseCode", "responseDescription","message","entity","responseBody","isTrusted","otp","responseInfo"})
@XmlType(propOrder = {"responseCode", "responseDescription" ,"message", "entity","responseBody","isTrusted","otp","responseInfo"})
public class ApiResponse<T> {

    protected String responseCode;
    protected String responseDescription;
    private T entity;
    protected String message;
    private Object responseBody;


    public ApiResponse() {
    }

    public ApiResponse(String responseCode, String responseDescription, T entity) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.entity = entity;
    }

    public ApiResponse(String responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public ApiResponse(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return Payload of the response.
     */
    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
    /**
     * @return Response code
     */
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return Description of the response. It is optional.
     */
    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }




    /**
     * @return Message of the response. It is optional.
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return ResponseBody of the response. It is optional.
     */
    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }



    @Override
    public String toString() {
        return String.format("ApiResponse[responseCode=%s,responseDescription=%s,entity=%s,message=%s]",
                responseCode, responseDescription, entity,message);
    }
}
