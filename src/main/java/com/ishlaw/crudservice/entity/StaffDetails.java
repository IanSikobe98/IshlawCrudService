package com.ishlaw.crudservice.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "staff_details")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StaffDetails.findAll", query = "SELECT s FROM StaffDetails s")})
public class StaffDetails implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "first_name")
    private String firstName;
    @Basic
    @Column(name = "surname")
    private String surname;
    @Basic
    @Column(name = "emailaddress")
    private String emailaddress;
    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;
    @Basic
    @Column(name = "date_updated")
    private Timestamp dateUpdated;
    @Basic
    @Column(name = "created_by")
    private int createdBy;
    @Basic
    @Column(name = "updated_by")
    private int updatedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffDetails that = (StaffDetails) o;
        return id == that.id && status == that.status && createdBy == that.createdBy && updatedBy == that.updatedBy && Objects.equals(firstName, that.firstName) && Objects.equals(surname, that.surname) && Objects.equals(emailaddress, that.emailaddress) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(password, that.password) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname, emailaddress, phoneNumber, password, status, dateCreated, dateUpdated, createdBy, updatedBy);
    }
}
