import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UsuariosDashboardComponent } from './usuarios/usuarios-dashboard.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, UsuariosDashboardComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Eventos y Servicios');
}
