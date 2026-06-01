package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.Modpack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModpackRepository extends JpaRepository<Modpack, Long> {
    List<Modpack> findByOwnerIdOrIsPublicTrue(Long ownerId);
}
