package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.exceptions.CustomPasswordException;
import org.springframework.stereotype.Component;

@Component
public class CredentialValidator {

    public void validatePassword(String password) {
        if (password.length() < 8) {
            throw new CustomPasswordException("Password must be atleast 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new CustomPasswordException("Password must contain an uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new CustomPasswordException("Password must contain a lowercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new CustomPasswordException("Password must contain a number");
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new CustomPasswordException("Password must contain a special character");
        }
    }

}
