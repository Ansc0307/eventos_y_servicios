import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Categoria } from '../models/categoria.model';
import { ChangeDetectorRef } from '@angular/core';
import { Oferta } from '../models/oferta.model';
import { CategoriasService } from '../services/categorias.service';
import { OfertasService } from '../services/ofertas.service';
import { OfertaCardComponent } from '../components/oferta-card/oferta-card.component';
import { CategoriaFilterComponent } from '../components/categoria-filter/categoria-filter.component';


@Component({
  selector: 'app-ofertas-page',
  standalone: true,
  templateUrl: './ofertas-page.component.html',
  imports: [
    CommonModule,
    CategoriaFilterComponent,  // <-- AGREGAR ESTO
    OfertaCardComponent
  ]
})
export class OfertasPageComponent implements OnInit {

  categorias: Categoria[] = [];
  ofertas: Oferta[] = [];
  selectedCategory: number | null = null;

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
    this.ofertasService.getOfertas().subscribe((data: Oferta[]) => {
      console.log('Ofertas recibidas (raw):', data);
      this.ofertas = data || [];
      console.log('Ofertas asignadas this.ofertas length=', this.ofertas.length);
      // Force change detection in case something runs outside zone
      try { this.cdr.detectChanges(); } catch (e) { /* noop */ }
    }, (err) => {
      console.error('Error cargando ofertas', err);
      this.ofertas = [];
    });
  }

  filterByCategory(catId: number) {
    this.selectedCategory = catId;
  }
}
