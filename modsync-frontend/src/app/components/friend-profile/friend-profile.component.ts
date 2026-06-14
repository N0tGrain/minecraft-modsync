import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Modpack } from '../../models/modpack.model';
import { ModpacksApiService } from '../../services/modpacks-api-service';

@Component({
  selector: 'app-friend-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './friend-profile.component.html',
  styleUrl: './friend-profile.component.scss',
})
export class FriendProfileComponent implements OnInit {
  protected readonly username = signal<string>('');
  protected readonly modpacks = signal<Modpack[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');

  constructor(
    private readonly modpacksApiService: ModpacksApiService,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id: number = Number(this.route.snapshot.paramMap.get('id'));
    const state = history.state as { username?: string } | undefined;
    if (state?.username) {
      this.username.set(state.username);
    }
    if (id) {
      this.loadModpacks(id);
    }
  }

  private loadModpacks(id: number): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.modpacksApiService.getUserModpacks(id).subscribe({
      next: (modpacks: Modpack[]): void => {
        this.modpacks.set(modpacks);
        if (!this.username() && modpacks.length > 0) {
          this.username.set(modpacks[0].ownerUsername);
        }
        this.isLoading.set(false);
      },
      error: (): void => {
        this.errorMessage.set("Failed to load this user's modpacks.");
        this.isLoading.set(false);
      },
    });
  }
}
