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
import { ProveedorReservasListComponent } from './reservas/proveedor-reservas-list.component';
import { OfertasPageComponent } from './pages/ofertas-page.component';
import { OfertaDetalleComponent } from './pages/oferta-detalle.component';
//import { OfertaDetailComponent } from './pages/oferta-detail.component';

export const routes: Routes = [
  { path: '', component: RoleDashboardComponent },
  { path: 'dashboard', component: RoleDashboardComponent },
  { path: 'dashboard/organizador', component: OrganizadorDashboardComponent },
  { path: 'dashboard/proveedor', component: ProveedorDashboardComponent },
  { path: 'proveedor/reservas', component: ProveedorReservasListComponent },
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
  path: 'ofertas',
  children: [
    { path: '', component: OfertasPageComponent },
    {
      path: ':id',
      loadComponent: () =>
        import('./pages/oferta-detalle.component').then(m => m.OfertaDetalleComponent)
    }
  ]
}


//{ path: 'ofertas/:id', component: OfertaDetailComponent },
//   { 
//     path: 'ofertas', 
//     loadChildren: () => import('./ofertas/ofertas.module').then(m => m.OfertasModule) 
//   },
//   { 
//     path: 'notificaciones', 
//     loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule) 
//   }
];