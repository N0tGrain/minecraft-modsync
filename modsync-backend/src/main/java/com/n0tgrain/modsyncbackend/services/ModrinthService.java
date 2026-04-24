package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.repositories.ModRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ModrinthService {

    private final RestTemplate restTemplate;
    private final ModRepository modRepository;

    public ModrinthService(RestTemplate restTemplate, ModRepository modRepository) {
        this.restTemplate = restTemplate;
        this.modRepository = modRepository;
    }

    public List<Mod> fetchAllMods() {
        return this.modRepository.findAll();
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
}
