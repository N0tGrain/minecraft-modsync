package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ModpackModId implements Serializable {
    private Long modpackId;
    private Long modVersionId;

    public ModpackModId(){}

    public ModpackModId(Long modpackId, Long modVersionId) {
        this.modpackId = modpackId;
        this.modVersionId = modVersionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModpackModId that = (ModpackModId) o;
        return Objects.equals(modpackId, that.modpackId) && Objects.equals(modVersionId, that.modVersionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modpackId, modVersionId);
    }
}
