import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ReservasListComponent } from './reservas/reservas-list.component';
import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';
import { NoDisponibilidadesListComponent } from './NoDisponibilidad/NoDisponibilidad-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ReservasListComponent, SolicitudesListComponent, NoDisponibilidadesListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Eventos y Servicios');
}
