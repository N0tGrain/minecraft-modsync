package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private CustomUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private CustomUser receiver;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public FriendRequest() {}

    public FriendRequest(CustomUser sender, CustomUser receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
