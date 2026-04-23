package com.n0tgrain.modsyncbackend.dtos;

import com.n0tgrain.modsyncbackend.models.Role;

public class UserResponse {

    public Long id;
    public String username;
    public String email;
    public Role role;

    public UserResponse(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
