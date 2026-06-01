package com.n0tgrain.modsyncbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.n0tgrain.modsyncbackend.dtos.AddModToModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackResponseDTO;
import com.n0tgrain.modsyncbackend.models.Modpack;
import com.n0tgrain.modsyncbackend.services.ModpackService;

import jakarta.validation.Valid;

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

    @GetMapping
    public List<ModpackResponseDTO> getModpacks() {
        return modpackService.getAccessibleModpacks();
    }

    @GetMapping("/user/{userId}")
    public List<ModpackResponseDTO> getUserModpacks(@PathVariable Long userId) {
        return modpackService.getUserModpacks(userId);
    }

    @GetMapping("/{id}")
    public ModpackResponseDTO getModpack(@PathVariable Long id) {
        return modpackService.getModpack(id);
    }

    @PutMapping("/{id}")
    public ModpackResponseDTO updateModpack(@PathVariable Long id, @Valid @RequestBody ModpackRequest request) {
        return modpackService.updateModpack(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteModpack(@PathVariable Long id) {
        modpackService.deleteModpack(id);
    }

    @PostMapping("/{id}/mods")
    public Modpack addMod(@PathVariable Long id, @Valid @RequestBody AddModToModpackRequest request) {
        return modpackService.addModToModpack(id, request);
    }
}
