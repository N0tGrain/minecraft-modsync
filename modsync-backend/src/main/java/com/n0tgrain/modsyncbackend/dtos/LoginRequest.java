package com.n0tgrain.modsyncbackend.dtos;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    public String username;

    @NotBlank
    public String password;

}
