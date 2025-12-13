import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Oferta } from '../../models/oferta.model';

@Component({
  selector: 'app-oferta-card-proveedor',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './oferta-card-proveedor.component.html',
  host: {
    class: 'block'
  }
})
export class OfertaCardProveedorComponent {
  @Input() oferta!: Oferta;

  constructor(private router: Router) {}

  openDetalle() {
    if (!this.oferta) return;
    this.router.navigate(['/ofertas', this.oferta.id]);
  }
}

