import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
//para notificaciones
import { NotificacionesMainComponent } from './components/notifications/notificaciones-main/notificaciones-main.component';
import { PrioridadesListComponent } from './components/notifications/prioridades-list/prioridades-list.component';
import { TiposListComponent } from './components/notifications/tipos-list/tipos-list.component';
import { NotificacionesListComponent } from './components/notifications/notificaciones-list/notificaciones-list.component';
import { ReservasListComponent } from './reservas/reservas-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },

  //rutas para notificaciones
  // Ruta principal de notificaciones
  { path: 'ms-notificaciones', component: NotificacionesMainComponent },
  { path: 'prioridades', component: PrioridadesListComponent },
  { path: 'tipos-notificacion', component: TiposListComponent},
  { path: 'notificaciones', component: NotificacionesListComponent },

//   { 
//     path: 'usuarios', 
//     loadChildren: () => import('./usuarios/usuarios.module').then(m => m.UsuariosModule) 
//   },
  { 
    path: 'reservas', 
    component: ReservasListComponent 
  },
//   { 
//     path: 'ofertas', 
//     loadChildren: () => import('./ofertas/ofertas.module').then(m => m.OfertasModule) 
//   },
//   { 
//     path: 'notificaciones', 
//     loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule) 
//   }
];