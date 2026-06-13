package com.n0tgrain.modsyncbackend.dtos;

import java.time.LocalDateTime;

public class FriendRequestDTO {
    public Long id;
    public Long senderId;
    public String senderUsername;
    public LocalDateTime createdAt;
}
