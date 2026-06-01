package com.n0tgrain.modsyncbackend.dtos;

import jakarta.validation.constraints.NotNull;

public class AddFriendRequest {
    @NotNull
    public Long friendId;
}
