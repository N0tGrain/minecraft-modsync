package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

@Entity
public class ModpackMod {

    @EmbeddedId
    private ModpackModId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modpackId")
    @JoinColumn(name = "modpack_id")
    private Modpack modpack;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modId")
    @JoinColumn(name = "mod_id")
    private Mod mod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_version_id")
    private ModVersion modVersion;

    public ModpackMod() {}

    public ModpackMod(Modpack modpack, Mod mod, ModVersion modVersion) {
        this.modpack = modpack;
        this.mod = mod;
        this.modVersion = modVersion;
    }
}
