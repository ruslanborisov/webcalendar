package com.webcalendar.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Roles")
public class UserRole implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="roles", nullable=false)
    @Enumerated(EnumType.STRING)
    private EnumRole role;

    public EnumRole getRole() {
        return role;
    }
    public void setRole(EnumRole role) {
        this.role = role;
    }

    public UserRole() {
    }

    public UserRole(Integer id, EnumRole role) {
        this.id = id;
        this.role = role;
    }

    public enum EnumRole {
        ROLE_USER, ROLE_ADMIN
    }
}
