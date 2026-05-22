package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.ModpackMod;
import com.n0tgrain.modsyncbackend.models.ModpackModId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModpackModRepository extends JpaRepository<ModpackMod, ModpackModId> {
    boolean existsByModpackIdAndModVersionId(Long modpackId, Long modVersionId);
}
