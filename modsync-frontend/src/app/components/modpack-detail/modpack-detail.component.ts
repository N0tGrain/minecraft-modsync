import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ModsApiService } from '../../services/mods-api-service';
import { AuthService } from '../../services/auth.service';
import { Modpack, ModpackModEntry } from '../../models/modpack.model';
import { Mod, ModVersion } from '../../models/mod.model';
import { ModpacksApiService } from '../../services/modpacks-api-service';
import { User } from '../../models/user.model';

interface ModWithCompatibleVersions extends Mod {
  compatibleVersions: ModVersion[];
}

@Component({
  selector: 'app-modpack-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './modpack-detail.component.html',
  styleUrl: './modpack-detail.component.scss',
})
export class ModpackDetailComponent implements OnInit {
  protected readonly modpack = signal<Modpack | null>(null);
  protected readonly availableMods = signal<ModWithCompatibleVersions[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly successMessage = signal('');
  protected readonly isOwner = signal(false);
  protected readonly addingModId = signal<number | null>(null);

  // Tracks which version the user picked in the dropdown for each mod (keyed by mod id).
  private readonly selectedVersions = new Map<number, number>();

  constructor(
    private readonly modpacksApiService: ModpacksApiService,
    private readonly modsApiService: ModsApiService,
    private readonly authService: AuthService,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id: number = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadModpack(id);
    }
  }

  onVersionSelect(modId: number, versionId: string): void {
    this.selectedVersions.set(modId, Number(versionId));
  }

  isAlreadyAdded(versionId: number): boolean {
    return this.modpack()?.mods?.some((entry: ModpackModEntry): boolean => entry.modVersionId === versionId) ?? false;
  }

  addModToModpack(modId: number): void {
    const modpack: Modpack | null = this.modpack();
    const versionId: number | undefined = this.selectedVersions.get(modId);

    if (!modpack || !versionId) {
      this.errorMessage.set('Please select a version first.');
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');
    this.addingModId.set(modId);

    this.modpacksApiService.addModToModpack(modpack.id, { modVersionId: versionId, required: true }).subscribe({
      next: (): void => {
        this.successMessage.set('Mod added to modpack!');
        this.addingModId.set(null);
        this.loadModpack(modpack.id);
      },
      error: (err: any): void => {
        this.errorMessage.set(err?.error?.message ?? 'Could not add mod to modpack.');
        this.addingModId.set(null);
      },
    });
  }

  private loadModpack(id: number): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.modpacksApiService.getModpack(id).subscribe({
      next: (modpack: Modpack): void => {
        this.modpack.set(modpack);
        this.checkOwnership(modpack);
        this.loadCompatibleMods(modpack);
      },
      error: (): void => {
        this.errorMessage.set('Failed to load modpack.');
        this.isLoading.set(false);
      },
    });
  }

  private checkOwnership(modpack: Modpack): void {
    this.authService.getCurrentUser().subscribe({
      next: (user: User): void => this.isOwner.set(user.username === modpack.ownerUsername),
      error: (): void => this.isOwner.set(false),
    });
  }

  private loadCompatibleMods(modpack: Modpack): void {
    this.modsApiService.fetchAllMods().subscribe({
      next: (mods: Mod[]): void => {
        const compatible = mods
          .map((mod: Mod) => ({
            ...mod,
            compatibleVersions: (mod.versions ?? []).filter(
              (version: ModVersion): boolean =>
                version.minecraftVersion?.toLowerCase() === modpack.minecraftVersion.toLowerCase() &&
                version.loader?.toLowerCase() === modpack.loader.toLowerCase()
            ),
          }))
          .filter((mod): boolean => mod.compatibleVersions.length > 0);
        this.availableMods.set(compatible);
        this.isLoading.set(false);
      },
      error: (): void => {
        this.errorMessage.set('Failed to load mods.');
        this.isLoading.set(false);
      },
    });
  }
}
