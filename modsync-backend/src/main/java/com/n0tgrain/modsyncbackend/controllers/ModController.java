package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.services.ModrinthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mods")
@CrossOrigin(origins = "http://localhost:4200")
public class ModController {

    private final ModrinthService modrinthService;

    public ModController(ModrinthService modrinthService) {
        this.modrinthService = modrinthService;
    }

    @GetMapping("/import")
    public Object searchMods(@RequestParam String query) {
        return modrinthService.importMods(query);
    }
}
