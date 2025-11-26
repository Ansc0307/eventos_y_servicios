import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ReservasListComponent } from './reservas/reservas-list.component';
import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ReservasListComponent, SolicitudesListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Eventos y Servicios');
}
