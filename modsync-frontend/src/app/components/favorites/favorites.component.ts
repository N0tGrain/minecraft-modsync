import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Mod } from '../../models/mod.model';
import { FavoritesApiService } from '../../services/favorites-api.service';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.scss',
})
export class FavoritesComponent implements OnInit {
  protected readonly favorites = signal<Mod[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');

  constructor(private readonly favoritesApiService: FavoritesApiService) {}

  ngOnInit(): void {
    this.loadFavorites();
  }

  removeFavorite(externalId: string): void {
    this.favoritesApiService.removeFavorite(externalId).subscribe({
      next: () => this.loadFavorites(),
      error: () => this.errorMessage.set('Failed to remove favorite.'),
    });
  }

  private loadFavorites(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.favoritesApiService.getFavorites().subscribe({
      next: (mods) => {
        this.favorites.set(mods);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load favorites.');
        this.isLoading.set(false);
      },
    });
  }
}
