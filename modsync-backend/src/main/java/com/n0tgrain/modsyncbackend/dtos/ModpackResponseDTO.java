package com.n0tgrain.modsyncbackend.dtos;

import com.n0tgrain.modsyncbackend.models.Visibility;

import java.util.List;

public class ModpackResponseDTO {
    public Long id;
    public String name;
    public String description;
    public String minecraftVersion;
    public String loader;
    public Visibility visibility;
    public String ownerUsername;
    public List<ModpackModEntryDTO> mods;
}
