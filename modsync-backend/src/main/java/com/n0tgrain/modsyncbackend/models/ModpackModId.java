package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ModpackModId implements Serializable {
    private Long modpackId;
    private Long modId;

    public ModpackModId(){}

    public ModpackModId(Long modpackId, Long modId) {
        this.modpackId = modpackId;
        this.modId = modId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModpackModId that = (ModpackModId) o;
        return Objects.equals(modpackId, that.modpackId) && Objects.equals(modId, that.modId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modpackId, modId);
    }
}
