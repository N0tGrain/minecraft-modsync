package com.n0tgrain.modsyncbackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n0tgrain.modsyncbackend.dtos.modrinth.ModrinthEnvDTO;
import com.n0tgrain.modsyncbackend.dtos.modrinth.ModrinthFileDTO;
import com.n0tgrain.modsyncbackend.dtos.modrinth.ModrinthIndexDTO;
import com.n0tgrain.modsyncbackend.exceptions.CustomModpackException;
import com.n0tgrain.modsyncbackend.models.*;
import com.n0tgrain.modsyncbackend.repositories.ModpackRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ModrinthExportService {

    private final ModpackRepository modpackRepository;
    private final ObjectMapper objectMapper;

    public ModrinthExportService(ModpackRepository modpackRepository) {
        this.modpackRepository = modpackRepository;
        this.objectMapper = new ObjectMapper();
    }

    public ModrinthIndexDTO generateModrinthIndex(Long modpackId) {
        Modpack modpack = modpackRepository.findById(modpackId)
                .orElseThrow(() -> new CustomModpackException("Modpack not found"));

        CustomUser currentUser = getAuthenticatedUser();
        if (!canAccessModpack(modpack, currentUser)) {
            throw new CustomModpackException("You do not have access to this modpack");
        }

        ModrinthIndexDTO index = new ModrinthIndexDTO();
        index.versionId = "1.0.0";
        index.name = modpack.getName();
        index.summary = modpack.getDescription();

        // Build dependencies
        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("minecraft", modpack.getMinecraftVersion());
        
        // Add loader dependency based on modpack loader
        String loaderName = mapLoaderName(modpack.getLoader());
        if (loaderName != null) {
            dependencies.put(loaderName, "");
        }
        index.dependencies = dependencies;

        // Build files from modpack mods
        List<ModrinthFileDTO> files = new ArrayList<>();
        for (ModpackMod modpackMod : modpack.getMods()) {
            ModVersion modVersion = modpackMod.getModVersion();
            if (modVersion != null && modVersion.getFileUrl() != null) {
                ModrinthFileDTO fileDto = new ModrinthFileDTO();
                
                // Set path: mods/{modname}-{version}.jar
                String fileName = sanitizeFileName(modVersion.getMod().getName(), modVersion.getVersion());
                fileDto.path = "mods/" + fileName;

                // Set hashes (placeholder for now - can be replaced with actual hash computation)
                Map<String, String> hashes = new HashMap<>();
                hashes.put("sha1", generatePlaceholderHash());
                hashes.put("sha512", generatePlaceholderHash());
                fileDto.hashes = hashes;

                // Set environment (client-required by default for mods)
                ModrinthEnvDTO env = new ModrinthEnvDTO();
                env.client = "required";
                env.server = modpackMod.isRequired() ? "required" : "optional";
                fileDto.env = env;

                // Set download URLs
                fileDto.downloads = new String[] { modVersion.getFileUrl() };

                // Set file size (placeholder - can be computed from URL)
                fileDto.fileSize = 0L;

                files.add(fileDto);
            }
        }
        index.files = files;

        return index;
    }

    public byte[] exportModpackAsZip(Long modpackId) throws Exception {
        ModrinthIndexDTO index = generateModrinthIndex(modpackId);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            // Add modrinth.index.json to zip
            ZipEntry indexEntry = new ZipEntry("modrinth.index.json");
            zos.putNextEntry(indexEntry);
            
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(index);
            zos.write(jsonContent.getBytes());
            zos.closeEntry();
        }
        
        return baos.toByteArray();
    }

    private String mapLoaderName(String loader) {
        if (loader == null) return null;
        
        return switch (loader.toLowerCase()) {
            case "fabric" -> "fabric-loader";
            case "forge" -> "forge";
            case "neoforge", "neo-forge" -> "neoforge";
            case "quilt" -> "quilt-loader";
            default -> null;
        };
    }

    private String sanitizeFileName(String modName, String version) {
        String name = modName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String ver = version != null ? version.replaceAll("[^a-zA-Z0-9._-]", "_") : "1.0";
        return name + "-" + ver + ".jar";
    }

    private String generatePlaceholderHash() {
        // Placeholder hash - should be replaced with actual computation if needed
        return "0000000000000000000000000000000000000000";
    }

    private CustomUser getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new CustomModpackException("Unauthorized");
        }
        if (!(authentication.getPrincipal() instanceof CustomUser user)) {
            throw new CustomModpackException("Invalid authentication principal");
        }
        return user;
    }

    private boolean canAccessModpack(Modpack modpack, CustomUser currentUser) {
        return modpack.getOwner().getId().equals(currentUser.getId()) || modpack.getVisibility() == Visibility.PUBLIC;
    }
}
