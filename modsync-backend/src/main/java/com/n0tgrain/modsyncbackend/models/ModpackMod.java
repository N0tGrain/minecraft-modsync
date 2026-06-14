package com.n0tgrain.modsyncbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ModpackMod {

    @EmbeddedId
    private ModpackModId id = new ModpackModId();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modpackId")
    @JoinColumn(name = "modpack_id")
    private Modpack modpack;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modVersionId")
    @JoinColumn(name = "mod_version_id")
    private ModVersion modVersion;

    private boolean required = true;

    public ModpackMod() {}

    public ModpackMod(Modpack modpack, ModVersion modVersion, boolean required) {
        this.modpack = modpack;
        this.modVersion = modVersion;
        this.required = required;
    }
}
