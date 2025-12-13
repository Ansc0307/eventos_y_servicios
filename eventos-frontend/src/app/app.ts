import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
//import { ReservasListComponent } from './reservas/reservas-list.component';
//import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';
//import { UsuariosDashboardComponent } from './usuarios/usuarios-dashboard.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { UsuariosService } from './services/usuarios.service';

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
export class App implements OnInit {
  title = 'eventos-frontend';

  constructor(private usuariosService: UsuariosService) {}

  ngOnInit(): void {
    // HU_5: al iniciar la app (después de Keycloak init), fuerza sincronización de verificación a BD.
    this.usuariosService.me().subscribe({
      next: () => {},
      error: () => {}
    });
  }
}

