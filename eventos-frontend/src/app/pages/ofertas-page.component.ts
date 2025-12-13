import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Categoria } from '../models/categoria.model';
import { ChangeDetectorRef } from '@angular/core';
import { Oferta } from '../models/oferta.model';
import { CategoriasService } from '../services/categorias.service';
import { OfertasService } from '../services/ofertas.service';
import { OfertaCardComponent } from '../components/oferta-card/oferta-card.component';
import { CategoriaFilterComponent } from '../components/categoria-filter/categoria-filter.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-ofertas-page',
  standalone: true,
  templateUrl: './ofertas-page.component.html',
  imports: [
    CommonModule,
    FormsModule,
    CategoriaFilterComponent,  // <-- AGREGAR ESTO
    OfertaCardComponent
  ]
})
export class OfertasPageComponent implements OnInit {

  categorias: Categoria[] = [];
  ofertasOriginales: Oferta[] = []; // 🔹 TODAS
  ofertas: Oferta[] = [];           
  selectedCategory: number | null = null;
  precioMin: number | null = null;
  precioMax: number | null = null;


  constructor(
    private categoriasService: CategoriasService,
    private ofertasService: OfertasService
    , private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCategorias();
    this.loadOfertas();
  }

  loadCategorias() {
    this.categoriasService.getCategorias().subscribe((data: Categoria[]) => {
    this.categorias = data;
    });
  }

  loadOfertas() {
  this.ofertasService.getOfertas().subscribe({
    next: (data: Oferta[]) => {
      this.ofertasOriginales = data || [];
      this.ofertas = [...this.ofertasOriginales]; // 👈 mostrar todo
      this.selectedCategory = null;

      try { this.cdr.detectChanges(); } catch {}
    },
    error: (err) => {
      console.error('Error cargando ofertas', err);
      this.ofertasOriginales = [];
      this.ofertas = [];
    }
  });
}


 filterByCategory(catId: number) {
  this.selectedCategory =
    this.selectedCategory === catId ? null : catId;
}
aplicarFiltros() {

  this.ofertas = this.ofertasOriginales.filter(o => {

    const cumpleCategoria =
      this.selectedCategory === null ||
      o.idCategoria === this.selectedCategory;

    const cumpleMin =
      this.precioMin === null ||
      o.precioBase >= this.precioMin;

    const cumpleMax =
      this.precioMax === null ||
      o.precioBase <= this.precioMax;

    return cumpleCategoria && cumpleMin && cumpleMax;
  });
}

limpiarFiltros() {
  this.selectedCategory = null;
  this.precioMin = null;
  this.precioMax = null;
  this.ofertas = [...this.ofertasOriginales];
}

}
