package com.ishlaw.crudservice.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "is_roles", schema = "ishfinal", catalog = "")
public class IsRoles {
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "intrash")
    private String intrash;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        IsRoles isRoles = (IsRoles) o;
        return intrash == isRoles.intrash && id == isRoles.id && Objects.equals(title, isRoles.title) && Objects.equals(description, isRoles.description) && Objects.equals(dateCreated, isRoles.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, intrash, id, dateCreated);
    }
}
