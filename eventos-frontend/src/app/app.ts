import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ReservasListComponent } from './reservas/reservas-list.component';
import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';
import { NavbarComponent } from './shared/navbar/navbar.component'; // ← Agregar esto
import { DashboardComponent } from './dashboard/dashboard.component'; // ← Agregar esto

@Component({
  selector: 'app-root',
  standalone: true, // ← Asegúrate de que esto esté
  imports: [
    RouterOutlet, 
    ReservasListComponent,
    SolicitudesListComponent,
    NavbarComponent,    // ← Agregar aquí
    DashboardComponent  // ← Agregar aquí
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  title = 'eventos-frontend';
}