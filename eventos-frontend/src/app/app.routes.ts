import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
//para notificaciones
import { NotificacionesMainComponent } from './notificaciones/pages/notificaciones-main/notificaciones-main.component';
import { NotificacionesListComponent } from './notificaciones/components/notificaciones-list/notificaciones-list.component';
import { PrioridadesListComponent } from './notificaciones/components/prioridades-list/prioridades-list.component';
import { TiposListComponent } from './notificaciones/components/tipos-list/tipos-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },

  //rutas para notificaciones
  // Ruta principal de notificaciones
  { path: 'notificaciones', component: NotificacionesMainComponent },
  
  // Sub-rutas de notificaciones
  { path: 'notificaciones/lista', component: NotificacionesListComponent },
  { path: 'notificaciones/prioridades', component: PrioridadesListComponent },
  { path: 'notificaciones/tipos', component: TiposListComponent },
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