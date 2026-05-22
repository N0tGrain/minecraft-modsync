package com.n0tgrain.modsyncbackend.dtos;

import jakarta.validation.constraints.NotBlank;

public class ModpackRequest {
    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotBlank
    public String minecraftVersion;

    @NotBlank
    public String loader;
}
