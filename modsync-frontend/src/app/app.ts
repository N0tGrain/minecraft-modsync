import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './components/navbar/navbar';

/**
 * Root component of the application.
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal<string>('modsync-frontend');
  protected isLoggedIn: boolean = false; //TODO: MOVE THIS TO AUTHENTICATION SERVICE
}
