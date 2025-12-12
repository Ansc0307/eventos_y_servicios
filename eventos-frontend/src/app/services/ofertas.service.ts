import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Oferta } from '../models/oferta.model';

@Injectable({
  providedIn: 'root'
})
export class OfertasService {

  // Use a relative path so the Angular dev server proxy can forward requests
  private baseUrl = '/ofertas/ofertas';

  constructor(private http: HttpClient) {}

  getOfertas(): Observable<Oferta[]> {
    // Backend returns a different shape; map it to our front-end model
    return this.http.get<any[]>(this.baseUrl).pipe(
      map((items) =>
        items.map((it) => ({
          id: it.idOfertas ?? it.id,
          proveedorId: it.proveedorId,
          titulo: it.titulo,
          idCategoria: it.categoria?.idCategoria ?? it.idCategoria ?? it.idCategoria,
          descripcion: it.descripcion,
          precioBase: it.precioBase,
          estado: it.estado,
          activo: it.activo,
          urlsMedia: (it.medias || it.urlsMedia || []).map((m: any) => m.url ?? m)
        } as Oferta))
      )
    );
  }

 getOfertaById(id: number): Observable<Oferta> {
  return this.http.get<any>(`${this.baseUrl}/${id}`).pipe(
    map((it) => {
      const categoria = it.categoria || it.idCategoria || null;

      // medias puede venir así:
      // [{ url: "xxx" }] o ["xxx"] o null
      const medias = it.medias ?? [];
      const urlsMedia = medias.map((m: any) => m.url ?? m);

      return {
        id: it.idOfertas ?? it.id,
        proveedorId: it.proveedorId,
        titulo: it.titulo,
        idCategoria: categoria?.idCategoria ?? categoria ?? null,
        categoria,
        descripcion: it.descripcion,
        precioBase: it.precioBase,
        estado: it.estado,
        activo: it.activo,
        medias,
        descuentos: it.descuentos ?? [],
        urlsMedia
      } as Oferta;
    })
  );
}
crearOferta(oferta: any): Observable<Oferta> {
  return this.http.post<any>(this.baseUrl, oferta).pipe(
    map((it) => ({
      id: it.idOfertas ?? it.id,
      proveedorId: it.proveedorId,
      titulo: it.titulo,
      idCategoria: it.categoria?.idCategoria ?? it.idCategoria ?? null,
      descripcion: it.descripcion,
      precioBase: it.precioBase,
      estado: it.estado,
      activo: it.activo,
      urlsMedia: (it.medias || it.urlsMedia || []).map((m: any) => m.url ?? m)
    } as Oferta))
  );
}
}
