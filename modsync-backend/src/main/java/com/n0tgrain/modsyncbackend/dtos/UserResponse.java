package com.n0tgrain.modsyncbackend.dtos;

public class UserResponse {

    public Long id;
    public String username;
    public String email;
    public String role;

    public UserResponse(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
