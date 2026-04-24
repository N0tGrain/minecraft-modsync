package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

@Entity
public class UserModpack {
    @EmbeddedId
    private UserModpackId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private CustomUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modpackId")
    @JoinColumn(name = "modpack_id")
    private Modpack modpack;

    public UserModpack() {}

    public UserModpack(CustomUser user, Modpack modpack) {
        this.user = user;
        this.modpack = modpack;
    }
}
