package com.n0tgrain.modsyncbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.n0tgrain.modsyncbackend.models.Modpack;
import com.n0tgrain.modsyncbackend.models.Visibility;

@Repository
public interface ModpackRepository extends JpaRepository<Modpack, Long> {
    List<Modpack> findByOwnerIdOrVisibility(Long ownerId, Visibility visibility);

    @Query("SELECT m FROM Modpack m WHERE m.owner.id = :userId AND (m.visibility = 'PUBLIC' OR m.visibility = 'FRIENDS_ONLY')")
    List<Modpack> findAccessibleModpacksByUser(@Param("userId") Long userId);
}
