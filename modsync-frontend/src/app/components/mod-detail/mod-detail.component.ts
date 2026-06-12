import { Component, OnInit, signal } from '@angular/core';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Mod } from '../../models/mod.model';
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
      next: (data: Mod) => {
        this.mod.set(data);
        this.isLoading.set(false);
      },
      error: () => {
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
}
