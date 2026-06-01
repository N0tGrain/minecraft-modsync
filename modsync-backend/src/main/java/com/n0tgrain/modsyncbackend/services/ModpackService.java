package com.n0tgrain.modsyncbackend.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.n0tgrain.modsyncbackend.dtos.AddModToModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackRequest;
import com.n0tgrain.modsyncbackend.dtos.ModpackResponseDTO;
import com.n0tgrain.modsyncbackend.exceptions.CustomModException;
import com.n0tgrain.modsyncbackend.exceptions.CustomModpackException;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.models.ModVersion;
import com.n0tgrain.modsyncbackend.models.Modpack;
import com.n0tgrain.modsyncbackend.models.ModpackMod;
import com.n0tgrain.modsyncbackend.models.ModpackModId;
import com.n0tgrain.modsyncbackend.models.Visibility;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import com.n0tgrain.modsyncbackend.repositories.ModVersionRepository;
import com.n0tgrain.modsyncbackend.repositories.ModpackModRepository;
import com.n0tgrain.modsyncbackend.repositories.ModpackRepository;

@Service
public class ModpackService {

    private final ModpackRepository modpackRepository;
    private final ModVersionRepository modVersionRepository;
    private final ModpackModRepository modpackModRepository;
    private final FriendService friendService;
    private final CustomUserRepository customUserRepository;

    public ModpackService(ModpackRepository modpackRepository, ModVersionRepository modVersionRepository, ModpackModRepository modpackModRepository, FriendService friendService, CustomUserRepository customUserRepository) {
        this.modpackRepository = modpackRepository;
        this.modVersionRepository = modVersionRepository;
        this.modpackModRepository = modpackModRepository;
        this.friendService = friendService;
        this.customUserRepository = customUserRepository;
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
        modpack.setVisibility(modpackRequest.visibility != null ? modpackRequest.visibility : Visibility.PRIVATE);
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
        response.visibility = modpack.getVisibility();
        response.ownerUsername = modpack.getOwner().getUsername();

        return response;
    }

    public Modpack addModToModpack(Long modpackId, AddModToModpackRequest request) {

        if (modpackModRepository.existsByModpackIdAndModVersionId(modpackId,request.modVersionId)) {
            throw new CustomModpackException("Mod version already added to modpack");
        }

        Modpack modpack = modpackRepository.findById(modpackId)
                .orElseThrow(() -> new CustomModpackException("Modpack not found"));
        ModVersion modVersion = modVersionRepository.findById(request.modVersionId)
                .orElseThrow(() -> new CustomModException("Mod version not found"));

        validateCompatibility(modpack, modVersion);
        boolean alreadyExists = modpack.getMods().stream().anyMatch(existing -> existing.getModVersion().getId().equals(modVersion.getId()));
        if (alreadyExists) {
            throw new CustomModpackException("Mod already added to the modpack");
        }

        ModpackMod modpackMod = new ModpackMod();

        modpackMod.setId(new ModpackModId(modpack.getId(), modVersion.getId()));

        modpackMod.setModpack(modpack);
        modpackMod.setModVersion(modVersion);
        modpackMod.setRequired(request.required);

        modpack.getMods().add(modpackMod);
        modpackRepository.save(modpack);

        return modpack;
    }

    public java.util.List<ModpackResponseDTO> getAccessibleModpacks() {
        CustomUser currentUser = getAuthenticatedUser();
        return modpackRepository.findAll().stream()
                .filter(modpack -> canAccessModpack(modpack, currentUser))
                .map(this::mapToResponse)
                .toList();
    }

    public ModpackResponseDTO getModpack(Long modpackId) {
        Modpack modpack = modpackRepository.findById(modpackId)
                .orElseThrow(() -> new CustomModpackException("Modpack not found"));
        CustomUser currentUser = getAuthenticatedUser();
        
        if (!canAccessModpack(modpack, currentUser)) {
            throw new CustomModpackException("Modpack is not accessible");
        }
        return mapToResponse(modpack);
    }

    public ModpackResponseDTO updateModpack(Long modpackId, ModpackRequest modpackRequest) {
        Modpack modpack = modpackRepository.findById(modpackId)
                .orElseThrow(() -> new CustomModpackException("Modpack not found"));
        CustomUser currentUser = getAuthenticatedUser();
        if (!modpack.getOwner().getId().equals(currentUser.getId())) {
            throw new CustomModpackException("You are not allowed to update this modpack");
        }

        modpack.setName(modpackRequest.name);
        modpack.setDescription(modpackRequest.description);
        modpack.setLoader(modpackRequest.loader);
        modpack.setMinecraftVersion(modpackRequest.minecraftVersion);
        modpack.setVisibility(modpackRequest.visibility != null ? modpackRequest.visibility : Visibility.PRIVATE);

        Modpack saved = modpackRepository.save(modpack);
        return mapToResponse(saved);
    }

    public void deleteModpack(Long modpackId) {
        Modpack modpack = modpackRepository.findById(modpackId)
                .orElseThrow(() -> new CustomModpackException("Modpack not found"));
        CustomUser currentUser = getAuthenticatedUser();
        if (!modpack.getOwner().getId().equals(currentUser.getId())) {
            throw new CustomModpackException("You are not allowed to delete this modpack");
        }
        modpackRepository.delete(modpack);
    }

    private CustomUser getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new CustomUserException("Unauthorized");
        }
        if (!(authentication.getPrincipal() instanceof CustomUser user)) {
            throw new CustomUserException("Invalid authentication principal");
        }
        return user;
    }

    private void validateCompatibility(Modpack modpack, ModVersion modVersion) {
        if (modVersion.getMinecraftVersion() == null ||
                !modVersion.getMinecraftVersion()
                        .equalsIgnoreCase(modpack.getMinecraftVersion())) {
            throw new CustomModpackException("Minecraft version mistmatch. " +
                    "Modpack uses " + modpack.getMinecraftVersion() +
                    " but mod supports " + modVersion.getMinecraftVersion());
        }

        if (modVersion.getLoader() == null ||
                !modVersion.getLoader()
                        .equalsIgnoreCase(modpack.getLoader())) {
            throw new CustomModpackException("Loader mismatch. " +
                    "Modpack uses " + modpack.getLoader() +
                    " but mod supports " + modVersion.getLoader());
        }
    }

    private boolean canAccessModpack(Modpack modpack, CustomUser currentUser) {
        // Owner can always access their own modpack
        if (modpack.getOwner().getId().equals(currentUser.getId())) {
            return true;
        }

        // PUBLIC modpacks are accessible to anyone
        if (modpack.getVisibility() == Visibility.PUBLIC) {
            return true;
        }

        // FRIENDS_ONLY modpacks are accessible only to friends
        if (modpack.getVisibility() == Visibility.FRIENDS_ONLY) {
            return friendService.areFriendsWith(modpack.getOwner().getId(), currentUser.getId());
        }

        // PRIVATE modpacks are not accessible
        return false;
    }

    public java.util.List<ModpackResponseDTO> getUserModpacks(Long userId) {
        CustomUser targetUser = customUserRepository.findById(userId)
                .orElseThrow(() -> new CustomUserException("User not found"));
        CustomUser currentUser = getAuthenticatedUser();

        return modpackRepository.findAccessibleModpacksByUser(userId).stream()
                .filter(modpack -> {
                    // Owner can see all their modpacks
                    if (modpack.getOwner().getId().equals(currentUser.getId())) {
                        return true;
                    }

                    // If modpack is PUBLIC, everyone can see it
                    if (modpack.getVisibility() == Visibility.PUBLIC) {
                        return true;
                    }

                    // If modpack is FRIENDS_ONLY, only friends can see it
                    if (modpack.getVisibility() == Visibility.FRIENDS_ONLY) {
                        return friendService.areFriendsWith(userId, currentUser.getId());
                    }

                    // PRIVATE modpacks are not visible
                    return false;
                })
                .map(this::mapToResponse)
                .toList();
    }
}
