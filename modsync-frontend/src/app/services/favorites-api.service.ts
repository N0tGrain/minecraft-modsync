import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mod } from '../models/mod.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FavoritesApiService {
  private readonly apiBaseUrl = environment.baseUrl + '/favorites';

  constructor(private readonly http: HttpClient) {}

  getFavorites(): Observable<Mod[]> {
    return this.http.get<Mod[]>(this.apiBaseUrl);
  }

  addFavorite(externalId: string): Observable<void> {
    return this.http.post<void>(`${this.apiBaseUrl}/${externalId}`, {});
  }

  removeFavorite(externalId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/${externalId}`);
  }

  isFavorite(externalId: string): Observable<{ favorite: boolean }> {
    return this.http.get<{ favorite: boolean }>(
      `${this.apiBaseUrl}/check/${externalId}`
    );
  }
}
