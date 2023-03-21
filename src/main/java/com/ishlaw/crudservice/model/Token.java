package com.ishlaw.crudservice.model;

import lombok.Data;

@Data
public class Token {
    private String firstName;
    private String secondName;
    private String emailAddress;
    private String msisdn;
    private Object team;
    private Integer id;

}
