package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.dtos.AuthResponse;
import com.n0tgrain.modsyncbackend.dtos.LoginRequest;
import com.n0tgrain.modsyncbackend.dtos.RegisterRequest;
import com.n0tgrain.modsyncbackend.models.RoleEnum;
import com.n0tgrain.modsyncbackend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return new AuthResponse("User registered successfully!", request.username, RoleEnum.USER.name());
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token, request.username, "USER");
    }
}
