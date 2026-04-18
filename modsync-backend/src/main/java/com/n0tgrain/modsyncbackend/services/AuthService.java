package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.config.JWTService;
import com.n0tgrain.modsyncbackend.dtos.LoginRequest;
import com.n0tgrain.modsyncbackend.dtos.RegisterRequest;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final CustomUserRepository customUserRepository;
    private final CredentialValidator credentialValidator;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(CustomUserRepository customUserRepository, CredentialValidator credentialValidator, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.customUserRepository = customUserRepository;
        this.credentialValidator = credentialValidator;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (customUserRepository.existsByUsername(request.username)) {
            throw new CustomUserException("Username already exists");
        }
        if (customUserRepository.existsByEmail(request.email)) {
            throw new CustomUserException("Email already exists");
        }

        credentialValidator.validatePassword(request.password);

        String hashedPassword = passwordEncoder.encode(request.password);

        CustomUser customUser = new CustomUser(request.username, request.email, hashedPassword);
        customUserRepository.save(customUser);
    }

    public String login(LoginRequest request) {
        CustomUser customUser = customUserRepository.findByUsername(request.username)
                .orElseThrow(() -> new CustomUserException("Invalid username of password"));

        if (!passwordEncoder.matches(request.password, customUser.getPassword())) {
            throw new CustomUserException("Invalid username or password");
        }

        return jwtService.generateToken(customUser.getUsername());
    }

    public List<CustomUser> getAllCustomUsers() {
        return customUserRepository.findAll();
    }

    public CustomUser getCustomUserById(Long id) {
        return customUserRepository.findById(id).orElse(null);
    }
}
