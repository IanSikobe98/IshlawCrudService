package com.ishlaw.crudservice.model;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Data
public class StaffDetailsDTO {

    private int id;

    private String firstName;

    private String surname;

    private String emailaddress;

    private String phoneNumber;

    private int status;

    private Date dateCreated;

    private Date dateUpdated;

    private int createdBy;

    private int updatedBy;
}