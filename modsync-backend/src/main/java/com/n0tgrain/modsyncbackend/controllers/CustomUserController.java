package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.dtos.UserResponse;
import com.n0tgrain.modsyncbackend.services.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomUserController {

    private final AuthService authService;

    public CustomUserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return authService.getAllCustomUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return authService.getCustomUserById(id);
    }

    // For testing purposes :D
    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return authService.getCurrentUser();
    }

}
