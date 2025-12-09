import { Routes } from '@angular/router';
//import { OfertasListComponent } from './ofertas/ofertas-list.component';
import { CategoriasListComponent } from './categorias/categorias-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'ofertas', pathMatch: 'full' },
  //{ path: 'ofertas', component: OfertasListComponent },
  { path: 'categorias', component: CategoriasListComponent },
  { path: '**', redirectTo: 'ofertas' }
];
