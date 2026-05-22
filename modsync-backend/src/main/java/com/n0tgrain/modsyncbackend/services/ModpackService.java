package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.dtos.ModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackResponseDTO;
import com.n0tgrain.modsyncbackend.exceptions.CustomModpackException;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.models.Modpack;
import com.n0tgrain.modsyncbackend.repositories.ModpackRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ModpackService {

    private final ModpackRepository modpackRepository;

    public ModpackService(ModpackRepository modpackRepository) {
        this.modpackRepository = modpackRepository;
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
}
