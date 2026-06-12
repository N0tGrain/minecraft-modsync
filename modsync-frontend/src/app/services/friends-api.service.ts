import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Friend } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class FriendsApiService {
  private readonly apiBaseUrl = environment.baseUrl + '/friends';

  constructor(private readonly http: HttpClient) {}

  getFriends(): Observable<Friend[]> {
    return this.http.get<Friend[]>(this.apiBaseUrl);
  }

  addFriend(friendId: number): Observable<void> {
    return this.http.post<void>(this.apiBaseUrl, { friendId });
  }

  removeFriend(friendId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/${friendId}`);
  }
}
