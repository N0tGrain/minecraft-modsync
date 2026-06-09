import { Component, OnInit, signal } from '@angular/core';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Mod } from '../../models/mod.model';
import { ModsApiService } from '../../services/mods-api-service';

/**
 * Component for displaying detailed information about a specific mod.
 * Implements SOLID principles:
 * - Single Responsibility: displays mod details only
 * - Dependency Injection: relies on ModsApiService and ActivatedRoute
 */
@Component({
  selector: 'app-mod-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './mod-detail.component.html',
  styleUrl: './mod-detail.component.scss',
})
export class ModDetailComponent implements OnInit {
  // Reactive state using Angular signals
  protected readonly mod = signal<Mod | null>(null);
  protected readonly isLoading = signal<boolean>(false);
  protected readonly errorMessage = signal<string>('');

  public constructor(
    private readonly modsApiService: ModsApiService,
    private readonly route: ActivatedRoute
  ) {}

  /**
   * Lifecycle hook: initializes component and loads mod details from route params
   */
  public ngOnInit(): void {
    const externalId: string | null = this.route.snapshot.paramMap.get('id');
    if (externalId) {
      this.loadModDetails(externalId);
    }
  }

  /**
   * Loads the details of a specific mod by its external ID.
   * @param externalId - The external ID of the mod
   * @private
   */
  private loadModDetails(externalId: string): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.modsApiService.fetchModByExternalId(externalId).subscribe({
      next: (data: Mod): void => {
        console.log(data);
        console.log(data.versions);
        this.mod.set(data);
        console.log(this.mod()!.versions!);
        this.isLoading.set(false);
      },
      error: (error: any): void => {
        console.error('Error loading mod details:', error);
        this.errorMessage.set(
          'Failed to load mod details. Please try again.'
        );
        this.isLoading.set(false);
      },
    });
  }

  /**
   * Handles retry action to reload mod details.
   */
  public retry(): void {
    const externalId: string | null = this.route.snapshot.paramMap.get('id');
    if (externalId) {
      this.loadModDetails(externalId);
    }
  }
}
