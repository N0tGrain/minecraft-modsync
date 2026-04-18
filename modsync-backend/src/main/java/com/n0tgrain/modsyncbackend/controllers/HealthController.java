package com.n0tgrain.modsyncbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String getHealth() {
        return "Backend is running!!";
    }

}
