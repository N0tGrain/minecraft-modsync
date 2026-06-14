import { Component, OnInit, signal } from '@angular/core';
import { Modpack, ModpackRequest, Visibility } from '../../models/modpack.model';
import { ModpacksApiService } from '../../services/modpacks-api-service';
import { ModsApiService } from '../../services/mods-api-service';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { Mod } from '../../models/mod.model';
import { User } from '../../models/user.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modpack-list',
  imports: [RouterLink, FormsModule],
  templateUrl: './modpack-list.html',
  styleUrl: './modpack-list.scss',
})
export class ModpackList implements OnInit {
  protected readonly visibility = Visibility;
  protected readonly modpacks = signal<Modpack[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly showCreateForm = signal(false);
  protected readonly isCreating = signal(false);
  protected readonly availableMinecraftVersions = signal<string[]>([]);
  protected readonly availableLoaders = signal<string[]>([]);

  protected newModpack: ModpackRequest = {
    name: '',
    description: '',
    minecraftVersion: '',
    loader: '',
    visibility: Visibility.PRIVATE,
  };

  constructor(
    private readonly modpacksApiService: ModpacksApiService,
    private readonly modsApiService: ModsApiService,
    private readonly authService: AuthService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.loadModpacks();
    this.loadVersionOptions();
  }

  protected toggleCreateForm(): void {
    this.showCreateForm.update((value: boolean): boolean => !value);
    this.errorMessage.set('');
  }

  protected createModpack(): void {
    if (
      !this.newModpack.name.trim() ||
      !this.newModpack.minecraftVersion.trim() ||
      !this.newModpack.loader.trim()
    ) {
      this.errorMessage.set('Name, Minecraft version and loader are required.');
      return;
    }
    this.isCreating.set(true);
    this.errorMessage.set('');
    this.modpacksApiService.createModpack(this.newModpack).subscribe({
      next: (modpack: Modpack) => this.router.navigate(['/modpacks', modpack.id]),
      error: (error: any): void => {
        this.errorMessage.set(error?.error?.message ?? 'Could not create modpack.');
        this.isCreating.set(false);
      },
    });
  }

  private loadModpacks(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');
    this.authService.getCurrentUser().subscribe({
      next: (currentUser: User): void => {
        this.modpacksApiService.getModpacks().subscribe({
          next: (modpacks: Modpack[]): void => {
            this.modpacks.set(
              modpacks.filter(
                (modpack: Modpack): boolean => modpack.ownerUsername === currentUser.username,
              ),
            );
            this.isLoading.set(false);
          },
          error: (): void => {
            this.errorMessage.set('Failed to load modpacks');
            this.isLoading.set(false);
          },
        });
      },
      error: (): void => {
        this.errorMessage.set('Failed to load profile');
        this.isLoading.set(false);
      },
    });
  }

  private loadVersionOptions(): void {
    this.modsApiService.fetchAllMods().subscribe({
      next: (mods: Mod[]): void => {
        const minecraftVersions = new Set<string>();
        const loaders = new Set<string>();

        for (const mod of mods) {
          for (const version of mod.versions ?? []) {
            if (version.minecraftVersion) {
              minecraftVersions.add(version.minecraftVersion);
            }
            if (version.loader) {
              loaders.add(version.loader);
            }
          }
        }
        this.availableMinecraftVersions.set([...minecraftVersions].sort());
        this.availableLoaders.set([...loaders].sort());
      },
    });
  }

  protected readonly Visibility = Visibility;
}
