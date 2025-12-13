import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { OfertasService } from '../services/ofertas.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CategoriasService } from '../services/categorias.service';
import { Categoria } from '../models/categoria.model';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './oferta-editar.component.html'
})
export class OfertaEditarComponent implements OnInit {

  form!: FormGroup;
  ofertaId!: number;
  categorias: any[] = [];
  imagenes: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private ofertasService: OfertasService,
    private categoriasService: CategoriasService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.ofertaId = Number(this.route.snapshot.paramMap.get('id'));

    this.form = this.fb.group({
    proveedorId: [1],
    titulo: [''],
    descripcion: [''],
    idCategoria: [null],
    precioBase: [null],
    estado: ['publicado'],
    activo: [true]
  });

  this.cargarCategorias();
  this.cargarOferta();
}
  cargarCategorias() {
  this.categoriasService.getCategorias().subscribe({
    next: (data: Categoria[]) => {
      console.log('CATEGORIAS RECIBIDAS:', data);
      this.categorias = data;
    },
    error: err => console.error('Error cargando categorías', err)
  });
}

cargarOferta() {
  this.ofertasService.getOfertaById(this.ofertaId).subscribe(oferta => {

    this.form.patchValue({
      proveedorId: oferta.proveedorId,
      titulo: oferta.titulo,
      descripcion: oferta.descripcion,
      precioBase: oferta.precioBase,
      activo: oferta.activo,
      estado: oferta.estado,

      // 👇 igual que crear-oferta
      idCategoria: oferta.idCategoria
    });
  });
}

  eliminar() {
    if (!confirm('¿Eliminar esta oferta?')) return;

    this.ofertasService.deleteOferta(this.ofertaId).subscribe({
      next: () => {
        alert('Oferta eliminada');
        this.router.navigate(['/proveedor/ofertas']);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/proveedor/ofertas']);
  }
  guardar() {
  const ofertaEditada = {
    ...this.form.value,
    urlsMedia: this.imagenes
  };

  this.ofertasService.editarOferta(this.ofertaId, ofertaEditada).subscribe({
    next: () => {
      alert('Oferta actualizada');
      this.router.navigate(['/proveedor/ofertas']);
    }
  });
}

}