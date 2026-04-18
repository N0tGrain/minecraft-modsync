package com.n0tgrain.modsyncbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username is required!")
    @Size(min=3, max=20, message = "Username must be 3-20 characters")
    public String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    public String password;
}
