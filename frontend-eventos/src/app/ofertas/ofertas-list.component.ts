import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model';
import { ChangeDetectorRef } from '@angular/core';
import { CategoriasService } from '../services/categorias.service';
import { Categoria } from '../models/categoria.model';

@Component({
  selector: 'app-ofertas-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="ofertas">
      <h2>Ofertas</h2>

      <!-- FORMULARIO CREAR OFERTA -->
      <form (ngSubmit)="crearOferta()" #ofertaForm="ngForm" class="crear">
        <div style="display:flex;gap:8px;flex-wrap:wrap;margin-bottom:0.75rem">
          <input name="proveedorId" [(ngModel)]="form.proveedorId" placeholder="ProveedorId (ej. 3)" type="number" required />
          <input name="titulo" [(ngModel)]="form.titulo" placeholder="Título" required />
          <select name="idCategoria" [(ngModel)]="form.idCategoria" required>
            <option [ngValue]="null">-- Seleccione categoría --</option>
            <option *ngFor="let c of categorias" [ngValue]="getCategoriaId(c)">
              {{ getCategoriaLabel(c) }}
            </option>
          </select>
          <input name="precioBase" [(ngModel)]="form.precioBase" placeholder="Precio" type="number" required />
          <input name="estado" [(ngModel)]="form.estado" placeholder="Estado (ej. publicado)" />
          <label style="display:flex;align-items:center;gap:6px;">
            <input type="checkbox" name="activo" [(ngModel)]="form.activo" /> Activo
          </label>
        </div>

        <div style="display:flex;gap:8px;flex-direction:column;margin-bottom:0.75rem">
          <textarea name="descripcion" [(ngModel)]="form.descripcion" placeholder="Descripción (opcional)"></textarea>
          <input name="urlsMediaRaw" [(ngModel)]="form.urlsMediaRaw" placeholder="URLs media (separadas por coma)" />
        </div>

        <div style="display:flex;gap:8px">
          <button class="btn" type="submit" [disabled]="creating">Crear oferta</button>
          <button class="btn secondary" type="button" (click)="limpiarForm()">Limpiar</button>
          <button class="btn" type="button" (click)="retry()" title="Recargar ofertas">Refrescar lista</button>
        </div>

        <div *ngIf="createError" class="error" style="margin-top:0.5rem">{{ createError }}</div>
        <div *ngIf="createSuccess" style="color:green;margin-top:0.5rem">{{ createSuccess }}</div>
      </form>

      <hr />

      <div *ngIf="loading">Cargando ofertas...</div>
      <div *ngIf="error" class="error">Error: {{ error }}</div>

      <table *ngIf="ofertas.length" class="tabla">
        <thead>
          <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Precio</th>
            <th>Categoría</th>
            <th>Activo</th>
            <th>Imagen</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let o of ofertas">
            <td>{{ getOfertaId(o) }}</td>
            <td>{{ o.titulo }}</td>
            <td>{{ o.precioBase | number:'1.2-2' }}</td>
            <td>{{ getCategoriaNombre(o) }}</td>
            <td>{{ o.activo ? 'Sí' : 'No' }}</td>
            <td>
            <div *ngIf="o.medias && o.medias.length > 0" 
                style="display:flex;gap:6px;flex-wrap:wrap">

                <img 
                *ngFor="let m of o.medias"
                [src]="m.url"
                style="width:80px;height:auto;border-radius:6px;border:1px solid #ddd"
                />

            </div>

            <span *ngIf="!o.medias || o.medias.length === 0">
                Sin imagen
            </span>
            </td>
          </tr>
        </tbody>
      </table>

      <div *ngIf="!loading && (!ofertas || ofertas.length === 0)" class="empty">No hay ofertas disponibles.</div>
    </section>
  `,
  styles: [
    `
      .ofertas { margin-top: 1rem; }
      .crear input, .crear textarea, .crear select { padding:6px 8px; border:1px solid #ddd; border-radius:6px; }
      .crear .btn { background: #7c3aed; color: white; padding:6px 10px; border-radius:6px; border:none; cursor:pointer }
      .crear .btn.secondary { background: transparent; border:1px solid #ddd; color: #333 }
      .tabla { width: 100%; border-collapse: collapse; margin-top: 0.75rem; }
      th, td { padding: 8px 12px; border: 1px solid #e0e0e0; text-align: left; }
      .error { color: #9b1c1c; margin-top: 0.5rem; }
      .empty { color: #666; margin-top: 0.5rem; }
    `
  ]
})
export class OfertasListComponent implements OnInit {
  ofertas: Oferta[] = [];
  loading = false;
  error: string | null = null;

  categorias: any[] = [];
  categoriaMap = new Map<number, string>();

  // estado del formulario
  form: any = {
    proveedorId: null,
    titulo: '',
    idCategoria: null,
    descripcion: '',
    precioBase: null,
    estado: 'publicado',
    activo: true,
    urlsMediaRaw: ''
  };
  creating = false;
  createError: string | null = null;
  createSuccess: string | null = null;

  constructor(
    private service: OfertasService,
    private categoriasService: CategoriasService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCategoriasThenOfertas();
  }

  // -----------------------
  // helpers para template
  // -----------------------
  getCategoriaId(c: any): number | null {
    if (!c) return null;
    // Normalizamos varias posibles key names
    const id = c.id ?? c.idCategoria ?? c.id_of_categoria ?? c.id_oferta ?? c.idOfCategoria ?? c.idOfertas ?? null;
    return id != null ? Number(id) : null;
  }

  getCategoriaLabel(c: any): string {
    if (!c) return '';
    const label = c.detalle ?? c.nombre ?? c.descripcion ?? (c.id != null ? `ID ${c.id}` : null);
    return label ?? String(this.getCategoriaId(c) ?? '');
  }

  getOfertaId(o: any): string {
    const id = o.id ?? o.idOferta ?? o.idOfertas ?? o.id_oferta ?? o.id_ofertas ?? null;
    return id != null ? String(id) : '';
  }

  getCategoriaNombre(o: any): string {
    // Si la API devuelve objeto 'categoria' dentro de la oferta:
    if (o?.categoria) {
      const catObj = o.categoria as any;
      return (catObj.detalle ?? catObj.nombre ?? catObj.id ?? '').toString();
    }
    // Si viene solo id de categoria:
    const catId = o.idCategoria ?? o.categoriaId ?? o.id_categoria ?? null;
    if (catId == null) return '';
    return this.categoriaMap.get(Number(catId)) ?? String(catId);
  }

  // -----------------------
  // carga de datos
  // -----------------------
  loadCategoriasThenOfertas() {
    this.loading = true;
    this.error = null;

    this.categoriasService.obtenerCategorias().subscribe({
      next: (cats) => {
        this.categorias = (cats || []) as any[];
        console.log('[OfertasList] categorías recibidas:', this.categorias);

        // construir mapa para lookup por id (si las categorias vienen con id)
        this.categoriaMap.clear();
        this.categorias.forEach(c => {
          const id = this.getCategoriaId(c);
          const nombre = this.getCategoriaLabel(c);
          if (id != null) this.categoriaMap.set(Number(id), nombre);
        });

        // opcional: auto-seleccionar la última categoría para pruebas
        if (this.categorias.length > 0 && (this.form.idCategoria == null)) {
          const last = this.categorias[this.categorias.length - 1];
          const lastId = this.getCategoriaId(last);
          if (lastId != null) {
            this.form.idCategoria = lastId;
            console.log('[OfertasList] set default idCategoria =>', this.form.idCategoria);
          }
        }

        // ahora cargar ofertas
        this.service.obtenerOfertas().subscribe({
          next: (data) => {
            console.log('[OfertasList] GET /api/ofertas response sample:', data?.[0]);
            this.ofertas = data || [];
            this.loading = false;
            try { this.cd.detectChanges(); } catch (e) {}
          },
          error: (err) => {
            console.error('[OfertasList] fetch error', err);
            this.error = err?.status === 404 ? 'No se encontraron ofertas' : (err?.message || 'Error desconocido');
            this.loading = false;
            try { this.cd.detectChanges(); } catch (e) {}
          }
        });
      },
      error: (err) => {
        console.error('[OfertasList] cargar categorias error', err);
        // aun así intentamos cargar ofertas aunque falte categorias
        this.service.obtenerOfertas().subscribe({
          next: (data) => {
            this.ofertas = data || [];
            this.loading = false;
            try { this.cd.detectChanges(); } catch (e) {}
          },
          error: (err2) => {
            this.error = err2?.message || 'Error desconocido';
            this.loading = false;
            try { this.cd.detectChanges(); } catch (e) {}
          }
        });
      }
    });
  }

  // -----------------------
  // crear oferta
  // -----------------------
  parseUrls(raw: string): string[] {
    if (!raw) return [];
    return raw.split(',').map(s => s.trim()).filter(s => s.length > 0);
  }

  crearOferta() {
    this.createError = null;
    this.createSuccess = null;

    // validaciones simples
    if (!this.form.proveedorId || !this.form.titulo || !this.form.precioBase) {
      this.createError = 'Completa proveedorId, título y precio.';
      return;
    }

    // Normalizar idCategoria (si el select devolvió objeto o número)
    let idCat = this.form.idCategoria;
    if (idCat && typeof idCat === 'object') {
      idCat = (idCat as any).id ?? (idCat as any).idCategoria ?? null;
    }
    const idCategoriaNum = idCat != null ? Number(idCat) : NaN;

    if (isNaN(idCategoriaNum)) {
      this.createError = 'Selecciona una categoría válida.';
      return;
    }

    const payload: any = {
      proveedorId: Number(this.form.proveedorId),
      titulo: this.form.titulo,
      idCategoria: idCategoriaNum,      // DTO espera idCategoria
      descripcion: this.form.descripcion || '',
      precioBase: Number(this.form.precioBase),
      estado: this.form.estado || 'publicado',
      activo: !!this.form.activo,
      urlsMedia: this.parseUrls(this.form.urlsMediaRaw)
    };

    console.log('[OfertasList] crear payload definitivo:', payload);
    this.creating = true;
    this.service.crearOferta(payload).subscribe({
      next: (created) => {
        console.log('[OfertasList] crear response:', created);
        this.createSuccess = 'Oferta creada correctamente';
        this.creating = false;
        this.limpiarForm();
        this.loadCategoriasThenOfertas();
      },
      error: (err) => {
        console.error('[OfertasList] crear error', err);
        this.createError = err?.error?.errors?.idCategoria 
                           ? `Categoría: ${err.error.errors.idCategoria}` 
                           : err?.error?.message || err?.message || 'Error creando oferta';
        this.creating = false;
      }
    });
  }

  limpiarForm() {
    this.form = {
      proveedorId: null,
      titulo: '',
      idCategoria: null,
      descripcion: '',
      precioBase: null,
      estado: 'publicado',
      activo: true,
      urlsMediaRaw: ''
    };
    this.createError = null;
    this.createSuccess = null;
  }

  retry() {
    this.loadCategoriasThenOfertas();
  }
}
