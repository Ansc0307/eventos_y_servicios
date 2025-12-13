import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { OfertasService } from '../../services/ofertas.service';
import { CategoriasService } from '../../services/categorias.service';
import { Categoria } from '../../models/categoria.model';
import { Oferta } from '../../models/oferta.model';

@Component({
  selector: 'app-crear-oferta',
  standalone: true,
  templateUrl: 'crear-oferta.component.html',
  imports: [CommonModule, ReactiveFormsModule]
})
export class CrearOfertaComponent implements OnInit {

  ofertaForm!: FormGroup;
  categorias: Categoria[] = [];
  imagenes: string[] = [];

  constructor(
    private fb: FormBuilder,
    private ofertasService: OfertasService,
    private categoriasService: CategoriasService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.ofertaForm = this.fb.group({
      proveedorId: [1, Validators.required],
      titulo: ['', Validators.required],
      descripcion: ['', Validators.required],

      // El usuario selecciona una categoría por su campo "id"
      idCategoria: [null, Validators.required],

      precioBase: [null, Validators.required],
      estado: ['publicado'],
      activo: [true]
    });

    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriasService.getCategorias().subscribe({
      next: (data) => {
        console.log("CATEGORIAS RECIBIDAS:", data);
        this.categorias = data;
      },
      error: (err) => {
        console.error('Error cargando categorías', err);
      }
    });
  }

  onImageAdd(event: any) {
    const files = event.target.files;

    for (let file of files) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagenes.push(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  }

crearOferta() {
    if (this.ofertaForm.invalid) {
      this.ofertaForm.markAllAsTouched();
      alert("Completa todos los campos obligatorios");
      return;
    }

    const nuevaOferta: Oferta = {
      id: 0, // Se ignora al crear
      ...this.ofertaForm.value,
      urlsMedia: this.imagenes
    };

    this.ofertasService.crearOferta(nuevaOferta).subscribe({
      next: () => {
        alert('Oferta creada con éxito');
        this.router.navigate(['/mis-ofertas']); // Redirigir al éxito
      },
      error: (err) => {
        console.error(err);
        alert("Ocurrió un error al crear la oferta");
      }
    });
  } // <--- 3. AQUÍ SE CERRABA MAL LA LLAVE

  // Botón "Guardar Cambios" del sidebar
  guardar() {
    // Reutilizamos la lógica de crearOferta
    this.crearOferta();
  }

  // Botón "Cancelar"
  cancelar() {
    this.router.navigate(['/mis-ofertas']); // O la ruta que prefieras
  }

  // Botón "Eliminar" (En contexto de crear, sería limpiar o descartar)
  eliminar() {
    if (!confirm('¿Estás seguro de descartar esta nueva oferta? Se perderán los datos.')) return;
    
    // Opción A: Limpiar formulario
    this.ofertaForm.reset({
      proveedorId: 1, 
      estado: 'publicado', 
      activo: true 
    });
    this.imagenes = [];
    
    // Opción B: Salir (si prefieres que eliminar te saque de la pantalla)
    // this.cancelar();
  }

}