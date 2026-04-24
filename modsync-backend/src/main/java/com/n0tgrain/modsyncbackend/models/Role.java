package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roleName; // ROLE_USER, ROLE_ADMIN

    private Long permissionLevel;

    public Role() {}

    public Role(String roleName, Long permissionLevel) {
        this.roleName = roleName;
        this.permissionLevel = permissionLevel;
    }
}
