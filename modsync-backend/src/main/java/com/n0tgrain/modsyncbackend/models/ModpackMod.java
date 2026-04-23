package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

@Entity
public class ModpackMod {

    @EmbeddedId
    private ModpackModId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modpackId")
    private Modpack modpack;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modId")
    private Mod mod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_version_id")
    private ModVersion modVersion;

}
