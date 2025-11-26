import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
//   { 
//     path: 'usuarios', 
//     loadChildren: () => import('./usuarios/usuarios.module').then(m => m.UsuariosModule) 
//   },
//   { 
//     path: 'reservas', 
//     loadChildren: () => import('./reservas/reservas.module').then(m => m.ReservasModule) 
//   },
//   { 
//     path: 'ofertas', 
//     loadChildren: () => import('./ofertas/ofertas.module').then(m => m.OfertasModule) 
//   },
//   { 
//     path: 'notificaciones', 
//     loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule) 
//   }
];