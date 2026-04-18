package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserService {

    private final CustomUserRepository customUserRepository;

    public CustomUserService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    public CustomUser createUser(String username, String email, String password) {
        CustomUser customUser = new CustomUser(username, email, password);
        return customUserRepository.save(customUser);
    }

    public List<CustomUser> getAllCustomUsers() {
        return customUserRepository.findAll();
    }

    public CustomUser getCustomUserById(Long id) {
        return customUserRepository.findById(id).orElse(null);
    }
}
