package com.n0tgrain.modsyncbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import com.n0tgrain.modsyncbackend.models.Visibility;

public class ModpackRequest {
    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotBlank
    public String minecraftVersion;

    @NotBlank
    public String loader;

    public Visibility visibility = Visibility.PRIVATE;
}
