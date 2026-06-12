import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  protected username = '';
  protected email = '';
  protected password = '';
  protected readonly errorMessage = signal('');
  protected readonly isLoading = signal(false);

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  onSubmit(): void {
    this.errorMessage.set('');
    this.isLoading.set(true);

    this.authService
      .register({ username: this.username, email: this.email, password: this.password })
      .subscribe({
        next: () => {
          this.isLoading.set(false);
          this.router.navigate(['/login']);
        },
        error: (err) => {
          const message =
            err?.error?.message ?? 'Registration failed. Please check your details.';
          this.errorMessage.set(message);
          this.isLoading.set(false);
        },
      });
  }
}
