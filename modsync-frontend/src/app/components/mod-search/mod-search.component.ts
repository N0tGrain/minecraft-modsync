import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Mod } from '../../models/mod.model';
import { ModsApiService } from '../../services/mods-api-service';

@Component({
  selector: 'app-mod-search',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mod-search.component.html',
  styleUrl: './mod-search.component.scss',
})
export class ModSearchComponent implements OnInit {
  protected readonly mods = signal<Mod[]>([]);
  protected readonly filteredMods = signal<Mod[]>([]);
  protected readonly importedMods = signal<Mod[]>([]);
  protected readonly displayMods = signal<Mod[]>([]);
  protected readonly searchQuery = signal<string>('');
  protected readonly isLoading = signal<boolean>(false);
  protected readonly isImporting = signal<boolean>(false);
  protected readonly errorMessage = signal<string>('');
  protected readonly importMessage = signal<string>('');

  constructor(private readonly modsApiService: ModsApiService) {}

  ngOnInit(): void {
    this.loadMods();
  }

  onSearchChange(query: string): void {
    this.searchQuery.set(query);
    this.filterMods(query);
  }

  importMods(): void {
    const query = this.searchQuery().trim();
    if (!query) {
      return;
    }

    this.isImporting.set(true);
    this.importMessage.set('');
    this.errorMessage.set('');

    this.modsApiService.fetchAllModsAndImport(query).subscribe({
      next: (imported: Mod[]) => {
        this.importedMods.set(imported);
        if (imported.length === 0) {
          this.importMessage.set('No new mods found on Modrinth for this search.');
        } else {
          this.importMessage.set(`Imported ${imported.length} new mod(s) from Modrinth.`);
        }
        this.loadMods();
        this.isImporting.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to import mods from Modrinth.');
        this.isImporting.set(false);
      },
    });
  }

  clearSearch(): void {
    this.searchQuery.set('');
    this.importedMods.set([]);
    this.importMessage.set('');
    this.filteredMods.set(this.mods());
    this.updateDisplayMods();
  }

  private loadMods(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.modsApiService.fetchAllMods().subscribe({
      next: (data: Mod[]) => {
        const sortedMods = [...data].sort((a, b) => b.downloads - a.downloads);
        this.mods.set(sortedMods);
        this.filterMods(this.searchQuery());
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load mods. Please try again.');
        this.isLoading.set(false);
      },
    });
  }

  private filterMods(query: string): void {
    if (!query.trim()) {
      this.filteredMods.set(this.mods());
      this.updateDisplayMods();
      return;
    }

    const lowerQuery = query.toLowerCase();
    const filtered = this.mods().filter(
      (mod) =>
        mod.name.toLowerCase().includes(lowerQuery) ||
        mod.description.toLowerCase().includes(lowerQuery)
    );
    this.filteredMods.set(filtered);
    this.updateDisplayMods();
  }

  private updateDisplayMods(): void {
    const importedIds = new Set(this.importedMods().map((m) => m.externalId));
    this.displayMods.set(
      this.filteredMods().filter((mod) => !importedIds.has(mod.externalId))
    );
  }
}
