import { Routes } from '@angular/router';
import { ModSearchComponent } from './components/mod-search/mod-search.component';
import { ModDetailComponent } from './components/mod-detail/mod-detail.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { authGuard } from './guards/auth.guard';
import { ModpackDetailComponent } from './components/modpack-detail/modpack-detail.component';
import { ModpackList } from './components/modpack-list/modpack-list';

export const routes: Routes = [
  {
    path: 'mods',
    component: ModSearchComponent,
    canActivate: [authGuard],
  },
  {
    path: 'mods/:id',
    component: ModDetailComponent,
    canActivate: [authGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard],
  },
  {
    path: 'favorites',
    component: FavoritesComponent,
    canActivate: [authGuard],
  },
  {
    path: 'modpacks',
    component: ModpackList,
    canActivate: [authGuard],
  },
  {
    path: 'modpacks/:id',
    component: ModpackDetailComponent,
    canActivate: [authGuard],
  },
  {
    path: '',
    redirectTo: 'mods',
    pathMatch: 'full',
  },
];
