import { Routes } from '@angular/router';
import { ModSearchComponent } from './components/mod-search/mod-search.component';
import { ModDetailComponent } from './components/mod-detail/mod-detail.component';

/**
 * Application routing configuration.
 */
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
    path: '',
    redirectTo: 'mods',
    pathMatch: 'full',
  },
];
