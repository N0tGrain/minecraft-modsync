import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FriendsApiService } from '../../services/friends-api.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit {
  protected isMenuOpen: boolean = false;
  protected readonly authService: AuthService = inject(AuthService);
  protected readonly friendService: FriendsApiService = inject(FriendsApiService);

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.friendService.refreshPendingRequestCount();
    }
  }

  protected toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  protected closeMenu(): void {
    this.isMenuOpen = false;
  }

  protected logout(): void {
    this.authService.logout();
    this.closeMenu();
  }
}
