package com.ishlaw.crudservice.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "is_permissions")
public class IsPermissions {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "permission_name")
    private String permissionName;
    @Basic
    @Column(name = "permission_description")
    private String permissionDescription;
    @Basic
    @Column(name = "is_menu")
    private int isMenu;
    @Basic
    @Column(name = "menu_level")
    private int menuLevel;
    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public int getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(int isMenu) {
        this.isMenu = isMenu;
    }

    public int getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
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
        IsPermissions that = (IsPermissions) o;
        return id == that.id && isMenu == that.isMenu && menuLevel == that.menuLevel && Objects.equals(permissionName, that.permissionName) && Objects.equals(permissionDescription, that.permissionDescription) && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permissionName, permissionDescription, isMenu, menuLevel, dateCreated);
    }
}
