package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ModpackModId implements Serializable {
    private Long modpackId;
    private Long modId;
}
