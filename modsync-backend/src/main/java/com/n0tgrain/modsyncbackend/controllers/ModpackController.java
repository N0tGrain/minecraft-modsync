package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.dtos.AddModToModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackResponseDTO;
import com.n0tgrain.modsyncbackend.models.Modpack;
import com.n0tgrain.modsyncbackend.services.ModpackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/modpacks")
@CrossOrigin(origins = "http://localhost:4200")
public class ModpackController {

    private final ModpackService modpackService;

    public ModpackController(ModpackService modpackService) {
        this.modpackService = modpackService;
    }

    @PostMapping
    public ModpackResponseDTO createModpack(@Valid @RequestBody ModpackRequest request) {
        return this.modpackService.createModpack(request);
    }

    @PostMapping("/{id}/mods")
    public Modpack addMod(@PathVariable Long id, @Valid @RequestBody AddModToModpackRequest request) {
        return modpackService.addModToModpack(id, request);
    }
}
