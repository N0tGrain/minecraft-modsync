import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Mod } from '../../models/mod.model';
import { ModsApiService } from '../../services/mods-api-service';

/**
 * Component for displaying and searching mods.
 * Implements SOLID principles:
 * - Single Responsibility: handles mod listing and search UI only
 * - Dependency Injection: relies on ModsApiService for data
 */
@Component({
  selector: 'app-mod-search',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mod-search.component.html',
  styleUrl: './mod-search.component.scss',
})
export class ModSearchComponent implements OnInit {
  // Reactive state using Angular signals
  protected readonly mods = signal<Mod[]>([]);
  protected readonly filteredMods = signal<Mod[]>([]);
  protected readonly searchQuery = signal<string>('');
  protected readonly isLoading = signal<boolean>(false);
  protected readonly errorMessage = signal<string>('');

  public constructor(private readonly modsApiService: ModsApiService) {}

  /**
   * Lifecycle hook: initializes component by loading mods
   */
  public ngOnInit(): void {
    this.loadMods();
  }

  /**
   * Loads all mods from the backend.
   * @private
   */
  protected loadMods(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.modsApiService.fetchAllMods().subscribe({
      next: (data: Mod[]): void => {

        const sortedMods: Mod[] = [...data].sort((a, b) => b.downloads - a.downloads);

        this.mods.set(sortedMods);
        this.filteredMods.set(data);
        this.isLoading.set(false);
      },
      error: (error: any): void => {
        console.error('Error loading mods:', error);
        this.errorMessage.set('Failed to load mods. Please try again.');
        this.isLoading.set(false);
      },
    });
    this.modsApiService.importVersionsForAllMods();
  }

  /**
   * Handles search input changes and filters the mod list.
   * @param query - The search query string
   */
  public onSearchChange(query: string): void {
    this.searchQuery.set(query);
    this.filterMods(query);
  }

  /**
   * Filters mods based on the search query.
   * @param query - The search query string
   * @private
   */
  private filterMods(query: string): void {
    const lowerQuery: string = query.toLowerCase();
    const filtered: Mod[] = this.mods().filter(
      (mod: Mod): boolean =>
        mod.name.toLowerCase().includes(lowerQuery) ||
        mod.description.toLowerCase().includes(lowerQuery)
    );
    this.filteredMods.set(filtered);
  }

  /**
   * Clears the search query and resets the mod list.
   */
  public clearSearch(): void {
    this.searchQuery.set('');
    this.filteredMods.set(this.mods());
  }
}
