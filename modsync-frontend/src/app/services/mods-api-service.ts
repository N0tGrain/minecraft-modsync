import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mod } from '../models/mod.model';

/**
 * Service for handling all mod-related API calls.
 * Implements SOLID principles: Single Responsibility (only handles mod API calls)
 */
@Injectable({
  providedIn: 'root',
})
export class ModsApiService {
  private readonly apiBaseUrl: string = 'http://localhost:8080/api/mods';

  public constructor(private readonly http: HttpClient) {}

  /**
   * Fetches all mods from the backend.
   * @returns Observable array of all mods
   */
  public fetchAllMods(): Observable<Mod[]> {
    return this.http.get<Mod[]>(`${this.apiBaseUrl}`);
  }

  /**
   * Fetches a specific mod by its external ID.
   * @param externalId - The external ID of the mod (Modrinth project ID)
   * @returns Observable containing the mod details
   */
  public fetchModByExternalId(externalId: string): Observable<Mod> {
    return this.http.get<Mod>(`${this.apiBaseUrl}/external/${externalId}`);
  }

  /**
   * Searches for mods by query string and imports them from Modrinth.
   * @param query - The search query string
   * @returns Observable containing search results (can be raw Modrinth data)
   */
  public searchMods(query: string): Observable<any> {
    return this.http.get<any>(`${this.apiBaseUrl}/import`, {
      params: { query },
    });
  }

  /**
   * Triggers the import of versions for all mods in the database.
   * @returns Observable containing the response message
   */
  public importVersionsForAllMods(): Observable<string> {
    return this.http.post<string>(`${this.apiBaseUrl}/import-versions`, {});
  }
}
