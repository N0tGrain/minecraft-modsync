package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

@Entity
public class UserModpack {
    @EmbeddedId
    private UserModpackId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private CustomUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modpackId")
    private Modpack modpack;
}
