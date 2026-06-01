package com.n0tgrain.modsyncbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.n0tgrain.modsyncbackend.dtos.AddFriendRequest;
import com.n0tgrain.modsyncbackend.dtos.FriendDTO;
import com.n0tgrain.modsyncbackend.services.FriendService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = "http://localhost:4200")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping
    public void addFriend(@Valid @RequestBody AddFriendRequest request) {
        friendService.addFriend(request.friendId);
    }

    @GetMapping
    public List<FriendDTO> getFriends() {
        return friendService.getFriends();
    }

    @DeleteMapping("/{friendId}")
    public void removeFriend(@PathVariable Long friendId) {
        friendService.removeFriend(friendId);
    }
}
