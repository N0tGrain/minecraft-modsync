package com.n0tgrain.modsyncbackend.dtos;

import jakarta.validation.constraints.NotNull;

public class AddModToModpackRequest {

    @NotNull
    public long modVersionId;
    public boolean required = true;
}
