import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria.model';

@Injectable({
  providedIn: 'root'
})
export class CategoriasService {

  private baseUrl = '/ofertas/categorias';

  constructor(private http: HttpClient) {}

  getCategorias(): Observable<Categoria[]> {
    // Use relative path so dev-server proxy can handle requests and avoid CORS
    return this.http.get<Categoria[]>(this.baseUrl);
  }
}
