import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CategoriasListComponent } from './categorias/categorias-list.component';
import { OfertasListComponent } from './ofertas/ofertas-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Eventos y Servicios');
}
