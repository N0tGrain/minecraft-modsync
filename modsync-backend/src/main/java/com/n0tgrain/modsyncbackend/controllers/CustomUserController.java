package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.services.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<CustomUser> getAllUsers() {
        return authService.getAllCustomUsers();
    }

    @GetMapping("/{id}")
    public CustomUser getUserById(@PathVariable Long id) {
        return authService.getCustomUserById(id);
    }

    // For testing purposes :D
    @GetMapping("/me")
    public CustomUser getCurrentUser() {
        return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
