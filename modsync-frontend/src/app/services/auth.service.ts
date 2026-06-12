import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  User,
} from '../models/user.model';

const TOKEN_KEY = 'modsync_token';
const USERNAME_KEY = 'modsync_username';
const ROLE_KEY = 'modsync_role';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiBaseUrl = environment.baseUrl + '/auth';
  private readonly usersBaseUrl = environment.baseUrl + '/users';

  private readonly tokenSignal = signal<string | null>(this.loadToken());
  private readonly usernameSignal = signal<string | null>(
    localStorage.getItem(USERNAME_KEY)
  );

  readonly isLoggedIn = computed(() => !!this.tokenSignal());
  readonly username = computed(() => this.usernameSignal());
  readonly token = computed(() => this.tokenSignal());

  constructor(private readonly http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiBaseUrl}/login`, request).pipe(
      tap((response) => this.persistAuth(response))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiBaseUrl}/register`, request);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    localStorage.removeItem(ROLE_KEY);
    this.tokenSignal.set(null);
    this.usernameSignal.set(null);
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.usersBaseUrl}/me`);
  }

  searchUsers(query: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.usersBaseUrl}/search`, {
      params: { query },
    });
  }

  private persistAuth(response: AuthResponse): void {
    if (response.token) {
      localStorage.setItem(TOKEN_KEY, response.token);
      this.tokenSignal.set(response.token);
    }
    localStorage.setItem(USERNAME_KEY, response.username);
    localStorage.setItem(ROLE_KEY, response.role);
    this.usernameSignal.set(response.username);
  }

  private loadToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }
}
