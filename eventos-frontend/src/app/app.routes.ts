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
import { OrganizadorReservasListComponent } from './reservas/organizador-reservas-list.component';
import { OrganizadorSolicitudesListComponent } from './solicitudes/organizador-solicitudes-list.component';
import { CalendarioDetalladoComponent } from './NoDisponibilidad/calendario_disponibilidad_prov';

import { authGuard } from './auth/auth.guard';
import { LandingComponent } from './auth/landing/landing.component';
import { SolicitudReservaFormComponent } from './solicitudes/solicitud-reserva-form.component';
import { OfertasPageComponent } from './pages/ofertas-page.component';
import { ProveedorOfertasPageComponent } from './pages/proveedor-ofertas-page.component';

export const routes: Routes = [
  // Ruta pública inicial
  { path: '', component: LandingComponent },

  // Ruta pública: registro (consume /usuarios/auth/register)
  {
    path: 'registro',
    loadComponent: () =>
      import('./auth/registro/registro.component').then((m) => m.RegistroComponent)
  },

  // Rutas privadas
  { path: 'dashboard', component: RoleDashboardComponent, canActivate: [authGuard] },
  {
    path: 'dashboard/organizador',
    component: OrganizadorDashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'dashboard/proveedor',
    component: ProveedorDashboardComponent,
    canActivate: [authGuard]
  },
  { path: 'proveedor/reservas', component: ProveedorReservasListComponent, canActivate: [authGuard] },
  { path: 'proveedor/solicitudes', component: ProveedorSolicitudesListComponent, canActivate: [authGuard] },
  { path: 'proveedor/no-disponibilidades', component: CalendarioDetalladoComponent, canActivate: [authGuard] },
  { path: 'reservas/organizador', component: OrganizadorReservasListComponent, canActivate: [authGuard] },
  { path: 'solicitudes/organizador', component: OrganizadorSolicitudesListComponent, canActivate: [authGuard] },
  // Ruta legacy previa
  { path: 'dashboard/legacy', component: DashboardComponent, canActivate: [authGuard] },

  //rutas para notificaciones
  // Ruta principal de notificaciones
  { path: 'ms-notificaciones', component: NotificacionesMainComponent, canActivate: [authGuard] },
  { path: 'prioridades', component: PrioridadesListComponent, canActivate: [authGuard] },
  { path: 'tipos-notificacion', component: TiposListComponent, canActivate: [authGuard] },
  { path: 'notificaciones', component: NotificacionesListComponent, canActivate: [authGuard] },

//   { 
//     path: 'usuarios', 
//     loadChildren: () => import('./usuarios/usuarios.module').then(m => m.UsuariosModule) 
//   },
  { 
    path: 'reservas', 
    component: ReservasListComponent,
    canActivate: [authGuard]
  },

    { 
    path: 'solicitudes', 
    component: SolicitudesListComponent,
    canActivate: [authGuard]
  },

{ 
    path: 'no-disponibilidades', 
    component: NoDisponibilidadesListComponent,
    canActivate: [authGuard]
  },

  {
    path: 'ofertas',
    canActivate: [authGuard],
    children: [
      { path: '', component: OfertasPageComponent, canActivate: [authGuard] },
      {
        path: ':id',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./pages/oferta-detalle.component').then((m) => m.OfertaDetalleComponent)
      }
    ]
  },

  {
    path: 'proveedor/ofertas/crear',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./components/crear-oferta/crear-oferta.component').then(
        (m) => m.CrearOfertaComponent
      )
  },
  { path: 'proveedor/ofertas', component: ProveedorOfertasPageComponent, canActivate: [authGuard] },
  {
    path: 'proveedor/ofertas/editar/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/oferta-editar.component').then((m) => m.OfertaEditarComponent)
  },
//   { 
//     path: 'ofertas', 
//     loadChildren: () => import('./ofertas/ofertas.module').then(m => m.OfertasModule) 
//   },
//   { 
//     path: 'notificaciones', 
//     loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule) 
//   },
  { path: 'solicitud-reserva', component: SolicitudReservaFormComponent, canActivate: [authGuard] }
];