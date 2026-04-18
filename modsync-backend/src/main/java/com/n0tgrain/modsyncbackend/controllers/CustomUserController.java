package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.dtos.CustomUserRequest;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.services.CustomUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomUserController {

    private final CustomUserService customUserService;

    public CustomUserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }


    @PostMapping
    public CustomUser createUser(@RequestBody CustomUserRequest request) {
        return customUserService.createUser(
                request.username,
                request.email,
                request.password
        );
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
