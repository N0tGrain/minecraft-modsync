import { Component, OnInit, signal } from '@angular/core';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Mod, ModVersion } from '../../models/mod.model';
import { ModsApiService } from '../../services/mods-api-service';
import { FavoritesApiService } from '../../services/favorites-api.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-mod-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './mod-detail.component.html',
  styleUrl: './mod-detail.component.scss',
})
export class ModDetailComponent implements OnInit {
  protected readonly mod = signal<Mod | null>(null);
  protected readonly isLoading = signal<boolean>(false);
  protected readonly errorMessage = signal<string>('');
  protected readonly isFavorite = signal<boolean>(false);
  protected readonly favoriteLoading = signal<boolean>(false);
  protected readonly selectedMinecraftVersion = signal<string>('');

  constructor(
    private readonly modsApiService: ModsApiService,
    private readonly favoritesApiService: FavoritesApiService,
    private readonly authService: AuthService,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const externalId = this.route.snapshot.paramMap.get('id');
    if (externalId) {
      this.loadModDetails(externalId);
      if (this.authService.isLoggedIn()) {
        this.loadFavoriteStatus(externalId);
      }
    }
  }

  toggleFavorite(): void {
    const modData = this.mod();
    if (!modData || !this.authService.isLoggedIn()) {
      return;
    }

    this.favoriteLoading.set(true);
    const action = this.isFavorite()
      ? this.favoritesApiService.removeFavorite(modData.externalId)
      : this.favoritesApiService.addFavorite(modData.externalId);

    action.subscribe({
      next: () => {
        this.isFavorite.update((v) => !v);
        this.favoriteLoading.set(false);
      },
      error: () => this.favoriteLoading.set(false),
    });
  }

  retry(): void {
    const externalId = this.route.snapshot.paramMap.get('id');
    if (externalId) {
      this.loadModDetails(externalId);
    }
  }

  protected isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  private loadModDetails(externalId: string): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.modsApiService.fetchModByExternalId(externalId).subscribe({
      next: (data: Mod): void => {
        this.mod.set(data);

        const versions: string[] = data.versions!.map(v => this.extractMajorVersion(v.minecraftVersion!));
        const uniqueVersions: string[] = [...new Set(versions)].sort((a: string, b: string): number => this.compareVersions(b, a));
        if (uniqueVersions.length > 0) {
          this.selectedMinecraftVersion.set(uniqueVersions[0]);
        }
        this.isLoading.set(false);
      },
      error: (): void => {
        this.errorMessage.set('Failed to load mod details. Please try again.');
        this.isLoading.set(false);
      },
    });
  }

  private loadFavoriteStatus(externalId: string): void {
    this.favoritesApiService.isFavorite(externalId).subscribe({
      next: (result) => this.isFavorite.set(result.favorite),
    });
  }

  protected onSelectedVersionChange(version: string): void {
    this.selectedMinecraftVersion.set(version);
  }

  protected getAvailableMinecraftVersions(): string[] {
    const modData = this.mod();
    if (!modData?.versions) {
      return [];
    }

    const versions = modData.versions.map(v => this.extractMajorVersion(v.minecraftVersion!));
    return [...new Set(versions)].sort((a,b) => this.compareVersions(b, a));
  }

  private extractMajorVersion(version: string): string {
    const parts: string[] = version.split('.');
    return parts.length >= 2 ? `${parts[0]}.${parts[1]}` : version;
  }

  protected getFilteredVersions(): ModVersion[] {
    const modData: Mod | null = this.mod();

    if (!modData?.versions) return [];

    const selected: string = this.selectedMinecraftVersion();

    if (!selected || selected === 'all') {
      return modData.versions;
    }

    return modData.versions.filter(version => version.minecraftVersion!.startsWith(selected));
  }

  private compareVersions(a: string, b: string): number {
    const aParts: number[] = a.split('.').map(Number);
    const bParts: number[] = b.split('.').map(Number);

    const max: number = Math.max(aParts.length, bParts.length);

    for (let i: number = 0; i < max; i++) {
      const aVal: number = aParts[i] || 0;
      const bVal: number = bParts[i] || 0;
      if (aVal !== bVal) {
        return aVal - bVal;
      }
    }
    return 0;
  }
}
