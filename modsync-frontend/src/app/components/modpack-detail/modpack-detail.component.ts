import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ModpacksApiService } from '../../services/modpacks-api-service';
import { ModsApiService } from '../../services/mods-api-service';
import { AuthService } from '../../services/auth.service';
import { Modpack, ModpackModEntry } from '../../models/modpack.model';
import { Mod, ModVersion } from '../../models/mod.model';

interface ModWithCompatibleVersion extends Mod {
  compatibleVersion: ModVersion | null;
}

@Component({
  selector: 'app-modpack-detail',
  imports: [RouterLink],
  templateUrl: './modpack-detail.component.html',
  styleUrl: './modpack-detail.component.scss',
})
export class ModpackDetailComponent implements OnInit {
  protected readonly modpack = signal<Modpack | null>(null);
  protected readonly availableMods = signal<ModWithCompatibleVersion[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly successMessage = signal('');
  protected readonly isOwner = signal(false);
  protected readonly addingModId = signal<number | null>(null);
  protected readonly removingModId = signal<number | null>(null);
  protected readonly deletingModpack = signal(false);
  protected readonly showAddMods = signal(false);

  constructor(
    private readonly modpacksApiService: ModpacksApiService,
    private readonly modsApiService: ModsApiService,
    private readonly authService: AuthService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadModpack(id);
    }
  }

  protected toggleAddMods(): void {
    this.showAddMods.update((value: boolean): boolean => !value);
    this.errorMessage.set('');
    this.successMessage.set('');
  }

  protected isAlreadyAdded(versionId: number): boolean {
    return this.modpack()?.mods?.some((entry) => entry.modVersionId === versionId) ?? false;
  }

  protected addModToModpack(mod: ModWithCompatibleVersion): void {
    const modpack = this.modpack();
    const version = mod.compatibleVersion;

    if (!modpack || !version) {
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');
    this.addingModId.set(mod.id);

    this.modpacksApiService.addModToModpack(modpack.id, { modVersionId: version.id, required: true }).subscribe({
      next: () => {
        this.successMessage.set('Mod added to modpack!');
        this.addingModId.set(null);
        this.loadModpack(modpack.id);
      },
      error: (error: any): void => {
        this.errorMessage.set(error?.error?.message ?? 'Could not add mod to modpack.');
        this.addingModId.set(null);
      },
    });
  }

  protected removeModFromModpack(entry: ModpackModEntry): void {
    const modpack = this.modpack();

    if (!modpack) {
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');
    this.removingModId.set(entry.modVersionId);

    this.modpacksApiService.removeModFromModpack(modpack.id, entry.modVersionId).subscribe({
      next: () => {
        this.successMessage.set(`${entry.modName} removed from modpack.`);
        this.removingModId.set(null);
        this.loadModpack(modpack.id);
      },
      error: (error: any): void => {
        this.errorMessage.set(error?.error?.message ?? 'Could not remove mod from modpack.');
        this.removingModId.set(null);
      },
    });
  }

  protected deleteModpack(): void {
    const modpack = this.modpack();

    if (!modpack) {
      return;
    }

    this.deletingModpack.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.modpacksApiService.deleteModpack(modpack.id).subscribe({
      next: () => {
        this.router.navigate(['/modpacks']);
      },
      error: () => {
        this.errorMessage.set('Failed to delete modpack.');
        this.deletingModpack.set(false);
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
      next: (user) => this.isOwner.set(user.username === modpack.ownerUsername),
      error: () => this.isOwner.set(false),
    });
  }

  private loadCompatibleMods(modpack: Modpack): void {
    this.modsApiService.fetchAllMods().subscribe({
      next: (mods: Mod[]): void => {
        const compatible = mods
          .map((mod) => ({
            ...mod,
            compatibleVersion:
              (mod.versions ?? []).find(
                (version) =>
                  version.minecraftVersion?.toLowerCase() === modpack.minecraftVersion.toLowerCase() &&
                  version.loader?.toLowerCase() === modpack.loader.toLowerCase(),
              ) ?? null,
          }))
          .filter((mod): mod is ModWithCompatibleVersion => mod.compatibleVersion !== null);

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
