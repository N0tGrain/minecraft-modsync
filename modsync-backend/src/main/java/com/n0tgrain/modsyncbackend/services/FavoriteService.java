package com.n0tgrain.modsyncbackend.services;

import com.n0tgrain.modsyncbackend.exceptions.CustomModException;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.models.UserFavoriteMod;
import com.n0tgrain.modsyncbackend.repositories.ModRepository;
import com.n0tgrain.modsyncbackend.repositories.UserFavoriteModRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final UserFavoriteModRepository userFavoriteModRepository;
    private final ModRepository modRepository;

    public FavoriteService(UserFavoriteModRepository userFavoriteModRepository, ModRepository modRepository) {
        this.userFavoriteModRepository = userFavoriteModRepository;
        this.modRepository = modRepository;
    }

    public List<Mod> getFavorites() {
        CustomUser currentUser = getAuthenticatedUser();
        return userFavoriteModRepository.findByUserId(currentUser.getId()).stream()
                .map(UserFavoriteMod::getMod)
                .toList();
    }

    public void addFavorite(String externalId) {
        CustomUser currentUser = getAuthenticatedUser();
        Mod mod = modRepository.findByExternalId(externalId)
                .orElseThrow(() -> new CustomModException("Mod not found"));

        if (userFavoriteModRepository.existsByUserIdAndModId(currentUser.getId(), mod.getId())) {
            throw new CustomUserException("Mod is already in favorites");
        }

        userFavoriteModRepository.save(new UserFavoriteMod(currentUser, mod));
    }

    public void removeFavorite(String externalId) {
        CustomUser currentUser = getAuthenticatedUser();
        Mod mod = modRepository.findByExternalId(externalId)
                .orElseThrow(() -> new CustomModException("Mod not found"));

        userFavoriteModRepository.deleteByUserIdAndModId(currentUser.getId(), mod.getId());
    }

    public boolean isFavorite(String externalId) {
        CustomUser currentUser = getAuthenticatedUser();
        return modRepository.findByExternalId(externalId)
                .map(mod -> userFavoriteModRepository.existsByUserIdAndModId(currentUser.getId(), mod.getId()))
                .orElse(false);
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
}
