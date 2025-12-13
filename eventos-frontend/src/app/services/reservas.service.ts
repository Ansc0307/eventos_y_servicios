import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
//import { Observable, timeout, catchError, throwError } from 'rxjs';
import { Reserva } from '../models/reserva.model';
import { Solicitud } from '../models/solicitud.model';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';

import { Observable, timeout, catchError, throwError, switchMap, of, EMPTY } from 'rxjs'; // 👈 AGREGAR switchMap, of, EMPTY

@Injectable({ providedIn: 'root' })
export class ReservasService {
  // use the frontend proxy: /ms-reservas -> http://localhost:8080/ms-reservas (gateway)
  private readonly base = '/ms-reservas/v1/reservas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.base);
  }

  getById(id: number): Observable<Reserva> {
    return this.http.get<Reserva>(`${this.base}/${id}`);
  }

  create(reserva: any): Observable<Reserva> {
    return this.http.post<Reserva>(this.base, reserva);
  }

  update(id: number, reserva: any): Observable<Reserva> {
    return this.http.put<Reserva>(`${this.base}/${id}`, reserva);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getByEstado(estado: string): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.base}/estado/${estado}`);
  }

  getByIdSolicitud(idSolicitud: number): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.base}/solicitud/${idSolicitud}`);
  }

  // Obtener reservas por proveedor (endpoint directo en backend)
  getByProveedor(idProveedor: number): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.base}/proveedor/${idProveedor}`);
  }

  // Opcional: Obtener reservas por organizador (paridad con backend)
  getByOrganizador(idOrganizador: number): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.base}/organizador/${idOrganizador}`);
  }

  getSolicitudByReservaId(idReserva: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.base}/${idReserva}/solicitud`).pipe(
      timeout(10000),
      catchError(err => {
        console.error('Error obteniendo solicitud por reserva:', err);
        return throwError(() => err);
      })
    );
  }

  getNoDisponibilidadByReserva(idReserva: number): Observable<NoDisponibilidad> {
    return this.http.get<NoDisponibilidad>(`${this.base}/${idReserva}/no-disponibilidad`).pipe(
      timeout(10000),
      catchError(err => {
        console.error('Error obteniendo no disponibilidad por reserva:', err);
        return throwError(() => err);
      })
    );
  }



  // ----------------------------------------------------------------------------------
  // 🆕 NUEVO: Método para buscar y eliminar reserva por ID de Solicitud (Encadenado)
  // ----------------------------------------------------------------------------------
  eliminarPorSolicitud(idSolicitud: number): Observable<void> {
    // 1. Buscar reservas por idSolicitud
    return this.getByIdSolicitud(idSolicitud).pipe(
      switchMap(reservas => {
        console.log('[ReservasService] Reservas encontradas para eliminar:', reservas);
        
        if (reservas.length > 0) {
          // 2. Si se encuentra, eliminar la primera reserva asociada
          const idReservaAEliminar = reservas[0].idReserva;
          console.log('[ReservasService] Eliminando reserva ID:', idReservaAEliminar);
          return this.delete(idReservaAEliminar); // Devuelve el Observable de DELETE
        }
        
        // Si no hay reservas, completar sin error
        console.log('[ReservasService] No se encontró reserva asociada. Terminando.');
        return EMPTY; 
      }),
      catchError(err => {
        // Si el DELETE devuelve 404, puede que ya haya sido eliminada.
        // Solo relanzamos si es un error inesperado.
        if (err.status === 404) {
          console.warn('[ReservasService] Intento de eliminar reserva falló con 404 (probablemente ya no existe).', err);
          return EMPTY; // Tratar como éxito para el flujo de rechazo
        }
        return throwError(() => err);
      })
    );
  }
// ----------------------------------------------------------------------------------
  // 🆕 NUEVO: Obtener reservas conflictivas por rango de fechas
  // ----------------------------------------------------------------------------------
  getReservasConflictivas(inicio: string, fin: string): Observable<Reserva[]> {
    // El backend espera ISO_LOCAL_DATE_TIME (yyyy-MM-ddTHH:mm:ss).
    // Asumimos que las fechas 'inicio' y 'fin' que le pasaremos ya vienen en el formato correcto (ISO string).
    return this.http.get<Reserva[]>(`${this.base}/conflictivas`, {
      params: {
        inicio: inicio,
        fin: fin
      }
    });
  }

// ... (código existente de ReservasService)

// ----------------------------------------------------------------------------------
// 🆕 NUEVO: Obtener todas las reservas (para bloquear fechas en el calendario)
// ----------------------------------------------------------------------------------
getTodasLasReservas(): Observable<Reserva[]> {
  return this.http.get<Reserva[]>(this.base); // Usamos el endpoint GET /v1/reservas
}

}
