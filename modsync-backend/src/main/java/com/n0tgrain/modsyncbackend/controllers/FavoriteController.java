package com.n0tgrain.modsyncbackend.controllers;

import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.services.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<Mod> getFavorites() {
        return favoriteService.getFavorites();
    }

    @GetMapping("/check/{externalId}")
    public Map<String, Boolean> isFavorite(@PathVariable String externalId) {
        return Map.of("favorite", favoriteService.isFavorite(externalId));
    }

    @PostMapping("/{externalId}")
    public void addFavorite(@PathVariable String externalId) {
        favoriteService.addFavorite(externalId);
    }

    @DeleteMapping("/{externalId}")
    public void removeFavorite(@PathVariable String externalId) {
        favoriteService.removeFavorite(externalId);
    }
}
