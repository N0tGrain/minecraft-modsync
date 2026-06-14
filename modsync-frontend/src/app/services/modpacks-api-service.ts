import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AddModToModpackRequest, Modpack, ModpackRequest } from '../models/modpack.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ModpacksApiService {

  private readonly apiBaseUrl: string = environment.baseUrl + '/modpacks';

  constructor(private http: HttpClient) {}

  public getModpacks(): Observable<Modpack[]> {
    return this.http.get<Modpack[]>(this.apiBaseUrl);
  }

  public getUserModpacks(userId: number): Observable<Modpack[]> {
    return this.http.get<Modpack[]>(`${this.apiBaseUrl}/user/${userId}`);
  }

  public getModpack(id: number): Observable<Modpack> {
    return this.http.get<Modpack>(`${this.apiBaseUrl}/${id}`);
  }

  public createModpack(request: ModpackRequest): Observable<Modpack> {
    return this.http.post<Modpack>(this.apiBaseUrl, request);
  }

  public updateModpack(id: number, request: ModpackRequest): Observable<Modpack> {
    return this.http.put<Modpack>(`${this.apiBaseUrl}/${id}`, request);
  }

  public deleteModpack(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/${id}`);
  }

  // TODO: instead of returning modpack, create DTO and handle that here
  // TODO: check if modpack is returned or if 'unknown' is needed
  public addModToModpack(modpackId: number, request: AddModToModpackRequest): Observable<Modpack> {
    return this.http.post<Modpack>(`${this.apiBaseUrl}/${modpackId}/mods`, request);
  }

}
