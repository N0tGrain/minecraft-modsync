package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserModpackId implements Serializable {
    private Long userId;
    private Long modpackId;
}
