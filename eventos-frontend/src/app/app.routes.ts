import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RoleDashboardComponent } from './dashboard/role-dashboard.component';
import { OrganizadorDashboardComponent } from './dashboard/organizador-dashboard.component';
import { ProveedorDashboardComponent } from './dashboard/proveedor-dashboard.component';
//para notificaciones
import { NotificacionesMainComponent } from './components/notifications/notificaciones-main/notificaciones-main.component';
import { PrioridadesListComponent } from './components/notifications/prioridades-list/prioridades-list.component';
import { TiposListComponent } from './components/notifications/tipos-list/tipos-list.component';
import { NotificacionesListComponent } from './components/notifications/notificaciones-list/notificaciones-list.component';
import { ReservasListComponent } from './reservas/reservas-list.component';
import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';
import { NoDisponibilidadesListComponent } from './NoDisponibilidad/NoDisponibilidad-list.component';
import { ProveedorReservasListComponent } from './reservas/proveedor-reservas-list.component';
import { ProveedorSolicitudesListComponent } from './solicitudes/proveedor-solicitudes-list.component';

export const routes: Routes = [
  { path: '', component: RoleDashboardComponent },
  { path: 'dashboard', component: RoleDashboardComponent },
  { path: 'dashboard/organizador', component: OrganizadorDashboardComponent },
  { path: 'dashboard/proveedor', component: ProveedorDashboardComponent },
  { path: 'proveedor/reservas', component: ProveedorReservasListComponent },
  { path: 'proveedor/solicitudes', component: ProveedorSolicitudesListComponent },
  // Ruta legacy previa
  { path: 'dashboard/legacy', component: DashboardComponent },

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

    { 
    path: 'solicitudes', 
    component: SolicitudesListComponent 
  },

{ 
    path: 'no-disponibilidades', 
    component: NoDisponibilidadesListComponent 
  },

  { path: 'ofertas', loadComponent: () => import('./ofertas/oferta-list.component').then(m => m.OfertaListComponent) },
//   { 
//     path: 'ofertas', 
//     loadChildren: () => import('./ofertas/ofertas.module').then(m => m.OfertasModule) 
//   },
//   { 
//     path: 'notificaciones', 
//     loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule) 
//   }
];