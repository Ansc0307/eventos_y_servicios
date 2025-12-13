import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UsuariosService } from '../../services/usuarios.service';
import { KeycloakService } from 'keycloak-angular';

type RolRegistro = 'ORGANIZADOR' | 'PROVEEDOR';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  private readonly fb = inject(FormBuilder);

  loading = false;
  errorMsg = '';
  success = false;
  private redirectTimer: any;

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    telefono: [''],
    rol: ['PROVEEDOR' as RolRegistro, [Validators.required]]
  });

  constructor(
    private usuariosService: UsuariosService,
    private keycloak: KeycloakService,
    private router: Router
  ) {}

  submit(): void {
    this.errorMsg = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.form.getRawValue();
    this.loading = true;
    this.usuariosService.register({
      nombre: payload.nombre!,
      email: payload.email!,
      password: payload.password!,
      telefono: payload.telefono || null,
      rol: payload.rol!
    }).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        // UX: mostrar mensaje y redirigir al login (Keycloak) automáticamente.
        this.redirectTimer = setTimeout(() => this.login(), 1500);
      },
      error: (err) => {
        this.loading = false;
        if (err?.status === 409) {
          this.errorMsg = 'El email ya está registrado.';
        } else if (err?.status === 400) {
          this.errorMsg = 'Revisa los datos del formulario.';
        } else {
          this.errorMsg = 'No se pudo registrar. Intenta de nuevo.';
        }
      }
    });
  }

  login(): void {
    this.keycloak.login({ redirectUri: window.location.origin + '/dashboard' });
  }

  volver(): void {
    this.router.navigateByUrl('/');
  }

  ngOnDestroy(): void {
    if (this.redirectTimer) {
      clearTimeout(this.redirectTimer);
    }
  }
}
