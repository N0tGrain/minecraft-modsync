package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.dtos.UserResponse;
import com.n0tgrain.modsyncbackend.services.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Clean this up later?
    @PreAuthorize("hasAuthority(T(com.n0tgrain.modsyncbackend.models.RoleEnum).ADMIN.getRoleName())")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return authService.getAllCustomUsers();
    }

    // Make sure only the admins or user themselves can do this
    @PreAuthorize("hasAuthority(T(com.n0tgrain.modsyncbackend.models.RoleEnum).USER.getRoleName())")
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
