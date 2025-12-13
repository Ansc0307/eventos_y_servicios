import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule, NgIf, NgFor, CurrencyPipe } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';
import { DescuentosService } from '../services/descuento.service';
import { SolicitudReservaFormComponent } from '../solicitudes/solicitud-reserva-form.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Descuento } from '../models/descuento.model';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-oferta-detalle',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    NgFor,
    CurrencyPipe,
    RouterModule,
    SolicitudReservaFormComponent,
    ReactiveFormsModule
  ],
  templateUrl: './oferta-detalle.component.html'
})
export class OfertaDetalleComponent implements OnInit {

  oferta: any = null;
  images: string[] = [];

  idOfertaActual?: number;

  proveedorActualId = 1; // 🔴 luego viene del Auth
  puedeEditar = false;
  mostrarModalDescuento = false;
  formDescuento!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private ofertaService: OfertasService,
    private descuentosService: DescuentosService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {}

  // =============================
  // INIT
  // =============================
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.idOfertaActual = isNaN(id) ? undefined : id;

    if (this.idOfertaActual) {
      this.cargarOferta();
    }
    this.formDescuento = this.fb.group({
      nombre: ['', Validators.required],
      tipoDescuento: ['porcentaje', Validators.required],
      valor: [0, [Validators.required, Validators.min(1)]],
      fechaInicio: ['', Validators.required],
      fechaFin: ['', Validators.required],
    });
  }

  // =============================
  // CARGAR OFERTA
  // =============================
  cargarOferta(): void {
    if (!this.idOfertaActual) return;

    this.ofertaService.getOfertaById(this.idOfertaActual).subscribe({
      next: (data: any) => {
        this.oferta = data;

        this.puedeEditar = data.proveedorId === this.proveedorActualId;

        if (data.medias?.length > 0) {
          this.images = data.medias.map((m: any) => m.url);
        } else if (data.urlsMedia?.length > 0) {
          this.images = data.urlsMedia;
        }

        this.cdr.detectChanges();
      },
      error: err => console.error('Error cargando oferta', err)
    });
  }

  // =============================
  // IMAGEN PRINCIPAL
  // =============================
  get mainImage(): string {
    return this.images.length > 0 ? this.images[0] : '';
  }
   // =============================
  // recargar oferta
  // =============================
  recargarOferta(): void {
  if (!this.idOfertaActual) return;

  this.ofertaService.getOfertaById(this.idOfertaActual).subscribe(data => {
    this.oferta = data;
    this.cdr.detectChanges();
  });
}

   // =============================
  // guardar descuento
  // =============================
guardarDescuento(): void {
  if (this.formDescuento.invalid || !this.oferta?.id) return;

  this.descuentosService
    .agregarDescuento(this.oferta.id, this.formDescuento.value)
    .subscribe({
      next: () => {
        this.cerrarModalDescuento();
        this.formDescuento.reset({ tipoDescuento: 'porcentaje' });
        this.recargarOferta();
      },
      error: err => {
        console.error('Error creando descuento', err);
        alert('No se pudo crear el descuento');
      }
    });
}

  // =============================
  // PRECIOS
  // =============================
  get precioFormateado(): string {
    return new Intl.NumberFormat('es-BO', {
      style: 'currency',
      currency: 'BOB'
    }).format(this.oferta?.precioBase || 0);
  }

  get descuentoActivo(): Descuento | null {
    if (!this.oferta?.descuentos?.length) return null;

    const ahora = new Date();

    return this.oferta.descuentos.find((d: Descuento) => {
      const inicio = new Date(d.fechaInicio);
      const fin = new Date(d.fechaFin);
      return ahora >= inicio && ahora <= fin;
    }) || null;
  }

  get precioConDescuento(): number {
    const d = this.descuentoActivo;
    if (!d) return this.oferta?.precioBase || 0;

    return d.tipoDescuento === 'porcentaje'
      ? this.oferta.precioBase * (1 - d.valor / 100)
      : this.oferta.precioBase - d.valor;
  }

  // =============================
  // DESCUENTOS
  // =============================
  agregarDescuento(descuento: Descuento): void {
    if (!this.idOfertaActual) return;

    this.descuentosService
      .agregarDescuento(this.idOfertaActual, descuento)
      .subscribe({
        next: () => {
          alert('Descuento agregado');
          this.cargarOferta();
        },
        error: err => {
          console.error('Error agregando descuento', err);
          alert('No se pudo agregar el descuento');
        }
      });
  }
abrirModalDescuento(): void {
  this.mostrarModalDescuento = true;
}

cerrarModalDescuento(): void {
  this.mostrarModalDescuento = false;
}

  // =============================
  // ELIMINAR OFERTA
  // =============================
  eliminarOferta(): void {
    if (!this.oferta?.id) return;

    if (!confirm('¿Eliminar esta oferta?')) return;

    this.ofertaService.deleteOferta(this.oferta.id).subscribe({
      next: () => {
        alert('Oferta eliminada');
        window.location.href = '/proveedor/ofertas';
      },
      error: err => {
        console.error('Error eliminando oferta', err);
        alert('No se pudo eliminar la oferta');
      }
    });
  }
}
