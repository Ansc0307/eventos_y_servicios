import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
//import { ReservasListComponent } from './reservas/reservas-list.component';
//import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';
import { NavbarComponent } from './shared/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true, // ← asegurarse que esto esté
  imports: [
    RouterOutlet, 
    NavbarComponent,    
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  title = 'eventos-frontend';
}