package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.services.ModrinthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mods")
@CrossOrigin(origins = "http://localhost:4200")
public class ModController {

    private final ModrinthService modrinthService;

    public ModController(ModrinthService modrinthService) {
        this.modrinthService = modrinthService;
    }

    @GetMapping
    public List<Mod> fetchMods() {
        return modrinthService.fetchAllMods();
    }

    @GetMapping("/{id}")
    public Mod fetchModByExternalId(@PathVariable String id) {
        return modrinthService.fetchByExternalId(id);
    }

    @GetMapping("/import")
    public Object searchMods(@RequestParam String query) {
        return modrinthService.importMods(query);
    }
}
