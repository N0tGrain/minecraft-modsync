import { Routes } from '@angular/router';
import { ModSearchComponent } from './components/mod-search/mod-search.component';
import { ModDetailComponent } from './components/mod-detail/mod-detail.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: 'mods',
    component: ModSearchComponent,
  },
  {
    path: 'mods/:id',
    component: ModDetailComponent,
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
    path: '',
    redirectTo: 'mods',
    pathMatch: 'full',
  },
];
