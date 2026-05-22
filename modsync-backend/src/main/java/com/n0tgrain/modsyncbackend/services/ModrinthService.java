package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.exceptions.CustomModException;
import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.models.ModVersion;
import com.n0tgrain.modsyncbackend.repositories.ModRepository;
import com.n0tgrain.modsyncbackend.repositories.ModVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ModrinthService {

    private final RestTemplate restTemplate;
    private final ModRepository modRepository;
    private final ModVersionRepository modVersionRepository;

    public ModrinthService(RestTemplate restTemplate, ModRepository modRepository, ModVersionRepository modVersionRepository) {
        this.restTemplate = restTemplate;
        this.modRepository = modRepository;
        this.modVersionRepository = modVersionRepository;
    }

    public List<Mod> fetchAllMods() {
        return this.modRepository.findAll();
    }

    public Mod fetchByExternalId(String externalId) {
        return modRepository.findByExternalId(externalId).orElseThrow(() -> new CustomModException("Mod with " + externalId + " not found in the db"));
    }

    public List<Mod> importMods(String query) {
        String url = "https://api.modrinth.com/v2/search?query=" + query;
        Map response = restTemplate.getForObject(url, Map.class);

        List<Map<String, Object>> hits = (List<Map<String, Object>>) response.get("hits");
        List<Mod> savedMods = new ArrayList<>();

        for (Map<String, Object> hit : hits) {
            if (!"mod".equals(hit.get("project_type"))) {
                continue;
            }
            String externalId = (String) hit.get("project_id");
            if (modRepository.existsByExternalId(externalId)) {
                continue;
            }

            Mod mod = mapToMod(hit);
            savedMods.add(modRepository.save(mod));
        }

        return savedMods;
    }

    private Mod mapToMod(Map<String, Object> hit) {

        Mod mod = new Mod();

        mod.setExternalId((String) hit.get("project_id"));
        mod.setName((String) hit.get("title"));
        mod.setSlug((String) hit.get("slug"));
        mod.setDescription((String) hit.get("description"));
        mod.setIconUrl((String) hit.get("icon_url"));
        mod.setDownloads((Integer) hit.get("downloads"));

        Object categoriesObj = hit.get("categories");
        if (categoriesObj instanceof List<?>) {
            List<String> categories = new ArrayList<>();
            for (Object obj : (List<?>) categoriesObj) {
                categories.add(obj.toString());
            }
            mod.setCategories(categories);
        }

        return mod;
    }

    public List<ModVersion> importVersions(Mod mod) {
        String url = "https://api.modrinth.com/v2/project/" + mod.getExternalId() + "/version";
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
        List<ModVersion> savedVersions = new ArrayList<>();

        for (Map<String, Object> versionData : response) {
            String externalVersionId = (String) versionData.get("id");
            if (modVersionRepository.existsByExternalVersionId(externalVersionId)) {
                continue;
            }
            ModVersion modVersion = mapToModVersion(versionData, mod);
            savedVersions.add(modVersionRepository.save(modVersion));
        }

        return savedVersions;
    }

    private ModVersion mapToModVersion(Map<String, Object> versionData, Mod mod) {
        ModVersion modVersion = new ModVersion();
        modVersion.setExternalVersionId((String) versionData.get("id"));
        modVersion.setVersion((String) versionData.get("version_number"));

        List<String> gameVersions = (List<String>) versionData.get("game_versions");
        if (gameVersions != null && !gameVersions.isEmpty()) {
            modVersion.setMinecraftVersion(gameVersions.getFirst()); // possibly normalize this later
        }

        List<String> loaders = (List<String>) versionData.get("loaders");
        if (loaders != null && !loaders.isEmpty()) {
            modVersion.setLoader(loaders.getFirst());
        }

        List<Map<String, Object>> files = (List<Map<String, Object>>) versionData.get("files");
        if (files != null && !files.isEmpty()) {
            modVersion.setFileUrl((String) files.getFirst().get("url"));
        }

        modVersion.setReleaseDate(Date.from(Instant.parse((String) versionData.get("date_published"))));
        modVersion.setMod(mod);

        return modVersion;
    }

    public void importVersionsForAllMods() {
        List<Mod> mods = modRepository.findAll();
        for (Mod mod : mods) {
            importVersions(mod);
        }
    }
}
