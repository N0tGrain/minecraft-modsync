package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.config.JWTService;
import com.n0tgrain.modsyncbackend.dtos.LoginRequest;
import com.n0tgrain.modsyncbackend.dtos.RegisterRequest;
import com.n0tgrain.modsyncbackend.dtos.UserResponse;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final CustomUserRepository customUserRepository;
    private final CredentialValidator credentialValidator;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final LoggerService loggerService;

    public AuthService(CustomUserRepository customUserRepository, CredentialValidator credentialValidator, PasswordEncoder passwordEncoder, JWTService jwtService, LoggerService loggerService) {
        this.customUserRepository = customUserRepository;
        this.credentialValidator = credentialValidator;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.loggerService = loggerService;
    }

    public void register(RegisterRequest request) {

        if (customUserRepository.existsByUsername(request.username)) {
            loggerService.logError(request.email + " tried to register with an already existing username");
            throw new CustomUserException("Username already exists");
        }
        if (customUserRepository.existsByEmail(request.email)) {
            loggerService.logError(request.username + " tried to register with an existing email");
            throw new CustomUserException("Email already exists");
        }

        credentialValidator.validatePassword(request.password);

        String hashedPassword = passwordEncoder.encode(request.password);

        CustomUser customUser = new CustomUser(request.username, request.email, hashedPassword);
        customUserRepository.save(customUser);
        loggerService.logInfo(customUser.getUsername() + " succesfully created an account");
    }

    public String login(LoginRequest request) {
        CustomUser customUser = customUserRepository.findByUsername(request.username)
                .orElseThrow(() -> new CustomUserException("Invalid username of password"));

        if (!passwordEncoder.matches(request.password, customUser.getPassword())) {
            loggerService.logError(customUser.getUsername() + "tried to login with incorrect details");
            throw new CustomUserException("Invalid username or password");
        }

        loggerService.logInfo(customUser.getUsername() + " succesfully logged in");
        return jwtService.generateToken(customUser.getUsername());
    }

    public List<UserResponse> getAllCustomUsers() {
        return customUserRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public UserResponse getCustomUserById(Long id) {
        CustomUser user = customUserRepository.findById(id).orElseThrow(() -> new CustomUserException("Custom user not found"));
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(CustomUser user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public UserResponse getCurrentUser() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mapToResponse(user);
    }
}
