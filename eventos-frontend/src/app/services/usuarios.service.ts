import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario, UsuarioCreateRequest, UsuarioUpdateRequest } from '../models/usuario.model';

interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class UsuariosService {
  // Proxy maps /api -> http://localhost:8080/ms-usuarios
  private readonly base = '/api/usuarios';

  constructor(private http: HttpClient) {}

  /**
   * HU_5: sincroniza el usuario autenticado.
   * Al llamar esto, el backend puede marcar activo=true si Keycloak ya tiene email_verified=true.
   */
  me(): Observable<Usuario> {
    return this.http.get<Usuario>('/usuarios/me');
  }

  register(payload: {
    nombre: string;
    email: string;
    password: string;
    telefono: string | null;
    rol: 'ORGANIZADOR' | 'PROVEEDOR';
  }): Observable<Usuario> {
    return this.http.post<Usuario>('/usuarios/auth/register', payload);
  }

  listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.base);
  }

  listarPaginado(params: {
    page?: number;
    size?: number;
    nombre?: string;
    email?: string;
    telefono?: string;
    rol?: string;
    activo?: boolean;
  }): Observable<Page<Usuario>> {
    let httpParams = new HttpParams();
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        httpParams = httpParams.set(key, String(value));
      }
    });
    return this.http.get<Page<Usuario>>(`${this.base}/page`, { params: httpParams });
  }

  crear(payload: UsuarioCreateRequest): Observable<Usuario> {
    return this.http.post<Usuario>(this.base, payload);
  }

  actualizarParcial(id: number, payload: UsuarioUpdateRequest): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.base}/${id}`, payload);
  }

  activar(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.base}/${id}/activar`, {});
  }

  desactivar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
