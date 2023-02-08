package com.ishlaw.crudservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 *
 * @author loco
 */
@JsonPropertyOrder(value = { "profile","responseCode", "responseDescription","data", "entity"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportResponse {
    @JsonProperty("pageProfile")
    private pageProfile profile;
    private List entity;
    private Object data;
    private String responseCode;
    private String responseDescription;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class pageProfile{

        private int pageNumber;
        private int  pageSize;
        private int totalPages;
        private int totalElements;
        private boolean last;
        private boolean first;

    }



}
