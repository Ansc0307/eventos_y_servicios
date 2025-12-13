import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule, NgIf, NgFor, CurrencyPipe } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';
import { DescuentosService } from '../services/descuento.service';
import { SolicitudReservaFormComponent } from '../solicitudes/solicitud-reserva-form.component';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Descuento } from '../models/descuento.model';
import { KeycloakService } from 'keycloak-angular';

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

  // ESTADO DE USUARIO
  isLoggedIn = false;
  esProveedor = false;
  usuarioIdActual: number = 1; // ID Hardcodeado temporalmente para pruebas

  puedeEditar = false;
  mostrarModalDescuento = false;
  formDescuento!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private ofertaService: OfertasService,
    private descuentosService: DescuentosService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private keycloak: KeycloakService
  ) {}

  async ngOnInit(): Promise<void> {
    // 1. Verificación de Auth
    this.isLoggedIn = await this.keycloak.isLoggedIn();
    
    if (this.isLoggedIn) {
      // Obtenemos todos los roles para depurar
      const roles = this.keycloak.getUserRoles();
      console.log('Roles del usuario:', roles);

      // Verificamos el rol (asegúrate que en Keycloak sea minúscula o ajusta aquí)
      // Probamos varias opciones comunes por si acaso
      this.esProveedor = this.keycloak.isUserInRole('proveedor') || 
                         this.keycloak.isUserInRole('PROVEEDOR') ||
                         this.keycloak.isUserInRole('provider');
                         
      console.log('¿Es proveedor?:', this.esProveedor);
    } else {
        console.log('Usuario NO logueado');
    }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.idOfertaActual = isNaN(id) ? undefined : id;

    if (this.idOfertaActual) {
      this.cargarOferta();
    }
    
    this.initForm();
  }

  initForm() {
    this.formDescuento = this.fb.group({
      nombre: ['', Validators.required],
      tipoDescuento: ['porcentaje', Validators.required],
      valor: [0, [Validators.required, Validators.min(1)]],
      fechaInicio: ['', Validators.required],
      fechaFin: ['', Validators.required],
    });
  }

  cargarOferta(): void {
    if (!this.idOfertaActual) return;

    this.ofertaService.getOfertaById(this.idOfertaActual).subscribe({
      next: (data: any) => {
        this.oferta = data;
        
        console.log('--- DEPURACIÓN PERMISOS ---');
        console.log('ID Proveedor en Oferta:', data.proveedorId, typeof data.proveedorId);
        console.log('ID Usuario Actual (Hardcode):', this.usuarioIdActual, typeof this.usuarioIdActual);
        console.log('Rol Proveedor:', this.esProveedor);

        // COMPARACIÓN SEGURA (Convierte ambos a String para comparar)
        const sonElMismoId = String(data.proveedorId) === String(this.usuarioIdActual);

        this.puedeEditar = this.isLoggedIn && this.esProveedor && sonElMismoId;
        
        console.log('RESULTADO FINAL -> puedeEditar:', this.puedeEditar);
        console.log('---------------------------');

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

  // ... (RESTO DE TUS MÉTODOS: mainImage, recargarOferta, guardarDescuento, etc. SIN CAMBIOS)
  
  get mainImage(): string {
    return this.images.length > 0 ? this.images[0] : '';
  }
  
  recargarOferta(): void {
    if (!this.idOfertaActual) return;
    this.ofertaService.getOfertaById(this.idOfertaActual).subscribe(data => {
      this.oferta = data;
      this.cdr.detectChanges();
    });
  }

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
  
  get precioFormateado(): string {
    return new Intl.NumberFormat('es-BO', { style: 'currency', currency: 'BOB' }).format(this.oferta?.precioBase || 0);
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
    return d.tipoDescuento === 'porcentaje' ? this.oferta.precioBase * (1 - d.valor / 100) : this.oferta.precioBase - d.valor;
  }

  abrirModalDescuento(): void { this.mostrarModalDescuento = true; }
  cerrarModalDescuento(): void { this.mostrarModalDescuento = false; }

  eliminarOferta(): void {
    if (!this.oferta?.id) return;
    if (!confirm('¿Eliminar esta oferta?')) return;
    this.ofertaService.deleteOferta(this.oferta.id).subscribe({
      next: () => {
        alert('Oferta eliminada');
        window.location.href = '/proveedor/ofertas'; // Ojo con esta ruta
      },
      error: err => {
        console.error('Error eliminando oferta', err);
        alert('No se pudo eliminar la oferta');
      }
    });
  }
}