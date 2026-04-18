package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserService {

    private final CustomUserRepository customUserRepository;
    private final CredentialValidator credentialValidator;
    private final PasswordEncoder passwordEncoder;

    public CustomUserService(CustomUserRepository customUserRepository, CredentialValidator credentialValidator, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.credentialValidator = credentialValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomUser createUser(String username, String email, String password) {

        if (customUserRepository.existsByUsername(username)) {
            throw new CustomUserException("Username already exists");
        }
        if (customUserRepository.existsByEmail(email)) {
            throw new CustomUserException("Email already exists");
        }

        credentialValidator.validatePassword(password);

        String hashedPassword = passwordEncoder.encode(password);

        CustomUser customUser = new CustomUser(username, email, hashedPassword);
        return customUserRepository.save(customUser);
    }

    public List<CustomUser> getAllCustomUsers() {
        return customUserRepository.findAll();
    }

    public CustomUser getCustomUserById(Long id) {
        return customUserRepository.findById(id).orElse(null);
    }
}
