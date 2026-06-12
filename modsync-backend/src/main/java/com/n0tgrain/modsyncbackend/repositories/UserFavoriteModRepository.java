package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.UserFavoriteMod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteModRepository extends JpaRepository<UserFavoriteMod, Long> {

    List<UserFavoriteMod> findByUserId(Long userId);

    Optional<UserFavoriteMod> findByUserIdAndModId(Long userId, Long modId);

    boolean existsByUserIdAndModId(Long userId, Long modId);

    void deleteByUserIdAndModId(Long userId, Long modId);
}
