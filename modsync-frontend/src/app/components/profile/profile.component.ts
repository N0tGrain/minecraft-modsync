import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FriendsApiService } from '../../services/friends-api.service';
import { Friend, FriendRequest, User } from '../../models/user.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  protected readonly user = signal<User | null>(null);
  protected readonly friends = signal<Friend[]>([]);
  protected readonly friendRequests = signal<FriendRequest[]>([]);
  protected readonly searchResults = signal<User[]>([]);
  protected readonly searchQuery = signal('');
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly successMessage = signal('');

  constructor(
    private readonly authService: AuthService,
    protected readonly friendsApiService: FriendsApiService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadFriends();
    this.loadFriendRequests();
  }

  onSearchChange(query: string): void {
    this.searchQuery.set(query);
    if (query.trim().length < 2) {
      this.searchResults.set([]);
      return;
    }

    this.authService.searchUsers(query.trim()).subscribe({
      next: (results) => this.searchResults.set(results),
      error: () => this.searchResults.set([]),
    });
  }

  sendFriendRequest(friendId: number): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    this.friendsApiService.sendFriendRequest(friendId).subscribe({
      next: () => {
        this.successMessage.set('Friend request sent!');
        this.searchResults.set([]);
        this.searchQuery.set('');
        // If the other user had already sent us a request, the backend
        // accepts it automatically and we become friends right away.
        this.loadFriends();
        this.loadFriendRequests();
      },
      error: () =>
        this.errorMessage.set(
          'Could not send friend request. You may already be friends or have a pending request.'
        ),
    });
  }

  acceptFriendRequest(requestId: number): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    this.friendsApiService.acceptFriendRequest(requestId).subscribe({
      next: () => {
        this.successMessage.set('Friend request accepted!');
        this.loadFriends();
        this.loadFriendRequests();
      },
      error: () => this.errorMessage.set('Could not accept friend request.'),
    });
  }

  declineFriendRequest(requestId: number): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    this.friendsApiService.declineFriendRequest(requestId).subscribe({
      next: () => this.loadFriendRequests(),
      error: () => this.errorMessage.set('Could not decline friend request.'),
    });
  }

  removeFriend(friendId: number): void {
    this.friendsApiService.removeFriend(friendId).subscribe({
      next: () => {
        this.loadFriends();
        this.successMessage.set('Friend succesfully removed!');
      },
      error: () => this.errorMessage.set('Could not remove friend.'),
    });
  }

  private loadProfile(): void {
    this.isLoading.set(true);
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.user.set(user);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load profile.');
        this.isLoading.set(false);
      },
    });
  }

  private loadFriends(): void {
    this.friendsApiService.getFriends().subscribe({
      next: (friends) => this.friends.set(friends),
      error: () => this.errorMessage.set('Failed to load friends.'),
    });
  }

  private loadFriendRequests(): void {
    this.friendsApiService.getFriendRequests().subscribe({
      next: (requests) => {
        this.friendRequests.set(requests);
        this.friendsApiService.pendingRequestCount.set(requests.length);
      },
      error: () => this.errorMessage.set('Failed to load friend requests.'),
    });
  }
}
