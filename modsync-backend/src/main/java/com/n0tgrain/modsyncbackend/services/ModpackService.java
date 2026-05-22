package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.dtos.AddModToModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackResponseDTO;
import com.n0tgrain.modsyncbackend.exceptions.CustomModException;
import com.n0tgrain.modsyncbackend.exceptions.CustomModpackException;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.*;
import com.n0tgrain.modsyncbackend.repositories.ModVersionRepository;
import com.n0tgrain.modsyncbackend.repositories.ModpackModRepository;
import com.n0tgrain.modsyncbackend.repositories.ModpackRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ModpackService {

    private final ModpackRepository modpackRepository;
    private final ModVersionRepository modVersionRepository;
    private final ModpackModRepository modpackModRepository;

    public ModpackService(ModpackRepository modpackRepository, ModVersionRepository modVersionRepository, ModpackModRepository modpackModRepository) {
        this.modpackRepository = modpackRepository;
        this.modVersionRepository = modVersionRepository;
        this.modpackModRepository = modpackModRepository;
    }

    public ModpackResponseDTO createModpack(ModpackRequest modpackRequest) {
        if (modpackRequest == null) {
            throw new CustomModpackException("Given data is invalid!");
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomUserException("Unauthorized");
        }
        if (!(authentication.getPrincipal() instanceof CustomUser user)) {
            throw new CustomUserException("Invalid authentication principal");
        }

        Modpack modpack = new Modpack();

        modpack.setName(modpackRequest.name);
        modpack.setDescription(modpackRequest.description);
        modpack.setLoader(modpackRequest.loader);
        modpack.setMinecraftVersion(modpackRequest.minecraftVersion);
        modpack.setOwner(user);

        Modpack saved = modpackRepository.save(modpack);

        return mapToResponse(saved);
    }

    private ModpackResponseDTO mapToResponse(Modpack modpack) {
        ModpackResponseDTO response = new ModpackResponseDTO();
        response.id = modpack.getId();
        response.name = modpack.getName();
        response.description = modpack.getDescription();
        response.minecraftVersion = modpack.getMinecraftVersion();
        response.loader = modpack.getLoader();
        response.ownerUsername = modpack.getOwner().getUsername();

        return response;
    }

    public Modpack addModToModpack(Long modpackId, AddModToModpackRequest request) {

        if (modpackModRepository.existsByModpackIdAndModVersionId(modpackId,request.modVersionId)) {
            throw new CustomModpackException("Mod version already added to modpack");
        }

        Modpack modpack = modpackRepository.findById(modpackId).orElseThrow(() -> new CustomModpackException("Modpack not found"));
        ModVersion modVersion = modVersionRepository.findById(request.modVersionId).orElseThrow(() -> new CustomModException("Mod version not found"));
        ModpackMod modpackMod = new ModpackMod();

        modpackMod.setId(new ModpackModId(modpack.getId(), modVersion.getId()));

        modpackMod.setModpack(modpack);
        modpackMod.setModVersion(modVersion);
        modpackMod.setRequired(request.required);

        modpack.getMods().add(modpackMod);
        //        modpackModRepository.save(modpackMod);

        return modpack;
    }
}
