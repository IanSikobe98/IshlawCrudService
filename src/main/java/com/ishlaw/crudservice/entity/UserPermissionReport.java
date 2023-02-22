package com.ishlaw.crudservice.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_permission_report")
public class UserPermissionReport {
    @Basic
    @Column(name = "id")
    @Id
    private long id;
    @Basic
    @Column(name = "first_name")
    private String firstName;
    @Basic
    @Column(name = "surname")
    private String surname;
    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic
    @Column(name = "role_id")
    private int roleId;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "permission_id")
    private int permissionId;
    @Basic
    @Column(name = "permission_name")
    private String permissionName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPermissionReport that = (UserPermissionReport) o;
        return id == that.id && roleId == that.roleId && permissionId == that.permissionId && Objects.equals(firstName, that.firstName) && Objects.equals(surname, that.surname) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(title, that.title) && Objects.equals(permissionName, that.permissionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname, phoneNumber, roleId, title, permissionId, permissionName);
    }
}
