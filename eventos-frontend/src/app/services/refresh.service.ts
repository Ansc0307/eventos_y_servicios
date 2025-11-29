import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

/**
 * Servicio compartido para notificar a los componentes cuando
 * los datos han sido actualizados (ej: cuando se crea un nuevo registro)
 */
@Injectable({
  providedIn: 'root'
})
export class RefreshService {
  // Sujetos para notificar cambios
  solicitudesRefresh$ = new Subject<void>();
  reservasRefresh$ = new Subject<void>();
  noDisponibilidadesRefresh$ = new Subject<void>();

  constructor() {}

  // Métodos para emitir eventos de refresco
  notificaSolicitudesChanged() {
    this.solicitudesRefresh$.next();
  }

  notificaReservasChanged() {
    this.reservasRefresh$.next();
  }

  notificaNoDisponibilidadesChanged() {
    this.noDisponibilidadesRefresh$.next();
  }
}
