package com.n0tgrain.modsyncbackend.dtos;

public class AuthResponse {

    public String token;
    public String username;
    public String role;

    public AuthResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
