import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Friend, FriendRequest, FriendRequestCount } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class FriendsApiService {
  private readonly apiBaseUrl: string = environment.baseUrl + '/friends';

  readonly pendingRequestCount = signal(0);

  constructor(private readonly http: HttpClient) {}

  getFriends(): Observable<Friend[]> {
    return this.http.get<Friend[]>(this.apiBaseUrl);
  }

  removeFriend(friendId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/${friendId}`);
  }

  sendFriendRequest(friendId: number): Observable<void> {
    return this.http.post<void>(`${this.apiBaseUrl}/requests`, { friendId });
  }

  getFriendRequests(): Observable<FriendRequest[]> {
    return this.http.get<FriendRequest[]>(`${this.apiBaseUrl}/requests`);
  }

  acceptFriendRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.apiBaseUrl}/requests/${requestId}/accept`, {});
  }

  declineFriendRequest(requestId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/requests/${requestId}`);
  }

  refreshPendingRequestCount(): void {
    this.http.get<FriendRequestCount>(`${this.apiBaseUrl}/requests/count`).subscribe({
      next: (result) => this.pendingRequestCount.set(result.count),
      error: () => this.pendingRequestCount.set(0),
    });
  }
}
