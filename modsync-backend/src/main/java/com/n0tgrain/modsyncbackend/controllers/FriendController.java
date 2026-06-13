package com.n0tgrain.modsyncbackend.controllers;

import java.util.List;

import com.n0tgrain.modsyncbackend.dtos.FriendRequestCountDTO;
import com.n0tgrain.modsyncbackend.dtos.FriendRequestDTO;
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
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    public List<FriendDTO> getFriends() {
        return friendService.getFriends();
    }

    @DeleteMapping("/{friendId}")
    public void removeFriend(@PathVariable Long friendId) {
        friendService.removeFriend(friendId);
    }

    @PostMapping("/requests")
    public void sendFriendRequest(@Valid @RequestBody AddFriendRequest request) {
        friendService.sendFriendRequest(request.friendId);
    }

    @GetMapping("/requests")
    public List<FriendRequestDTO> getFriendRequests() {
        return friendService.getPendingRequests();
    }

    @GetMapping("/requests/count")
    public FriendRequestCountDTO getFriendRequestCount() {
        return friendService.getPendingRequestCount();
    }

    @PostMapping("/requests/{requestId}/accept")
    public void acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
    }

    @DeleteMapping("/requests/{requestId}")
    public void declineFriendRequest(@PathVariable Long requestId) {
        friendService.declineFriendRequest(requestId);
    }
}
