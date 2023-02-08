package com.ishlaw.crudservice.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "role_map")
public class RoleMap {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "role_id")
    private int roleId;
    @Basic
    @Column(name = "user_id")
    private int userId;
    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleMap roleMap = (RoleMap) o;
        return id == roleMap.id && roleId == roleMap.roleId && userId == roleMap.userId && Objects.equals(dateCreated, roleMap.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleId, userId, dateCreated);
    }
}
