package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.dtos.AuthResponse;
import com.n0tgrain.modsyncbackend.dtos.LoginRequest;
import com.n0tgrain.modsyncbackend.dtos.RegisterRequest;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService customUserService;

    public AuthController(AuthService customUserService) {
        this.customUserService = customUserService;
    }


    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        customUserService.register(request);
        return new AuthResponse("User registered successfully!");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        customUserService.login(request);
        return new AuthResponse("User logged in successfully!");
    }

    @GetMapping
    public List<CustomUser> getAllUsers() {
        return customUserService.getAllCustomUsers();
    }

    @GetMapping("/{id}")
    public CustomUser getUserById(@PathVariable Long id) {
        return customUserService.getCustomUserById(id);
    }
}
