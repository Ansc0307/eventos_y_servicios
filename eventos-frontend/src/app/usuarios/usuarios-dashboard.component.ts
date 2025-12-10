import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuariosService } from '../services/usuarios.service';
import { Usuario, UsuarioCreateRequest, UsuarioUpdateRequest } from '../models/usuario.model';

@Component({
  selector: 'app-usuarios-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="usuarios">
      <h2>Usuarios (ms-usuarios)</h2>

      <div class="section">
        <div class="section-header">
          <h3>Listado</h3>
          <div class="section-actions">
            <input type="search" placeholder="Buscar por nombre o email" [(ngModel)]="filtro" name="filtro" />
            <button type="button" class="btn" (click)="refrescar()" [disabled]="cargandoLista()">Refrescar</button>
          </div>
        </div>

        <div *ngIf="cargandoLista()" class="info">Cargando usuarios...</div>
        <div *ngIf="errorLista()" class="error">{{ errorLista() }}</div>

        <div class="table-wrapper" *ngIf="!cargandoLista() && usuariosFiltrados().length">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Email</th>
                <th>Rol</th>
                <th>Teléfono</th>
                <th>Activo</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let u of usuariosFiltrados()" [class.inactive]="!u.activo">
                <td>{{ u.id }}</td>
                <td>{{ u.nombre }}</td>
                <td>{{ u.email }}</td>
                <td>{{ u.rol }}</td>
                <td>{{ u.telefono || '—' }}</td>
                <td>
                  <span class="badge" [class.badge-active]="u.activo" [class.badge-inactive]="!u.activo">
                    {{ u.activo ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>
                <td>
                  <button type="button" class="btn small" (click)="seleccionar(u)">Editar</button>
                  <button type="button" class="btn small secondary" (click)="alternarEstado(u)" [disabled]="cargandoToggleId() === u.id">
                    {{ u.activo ? 'Desactivar' : 'Activar' }}
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div *ngIf="!cargandoLista() && !usuariosFiltrados().length" class="info">No hay usuarios para mostrar.</div>
      </div>

      <div class="grid">
        <div class="section">
          <h3>Crear nuevo usuario</h3>
          <form (ngSubmit)="crearUsuario()" class="form">
            <label>
              Nombre completo
              <input type="text" [(ngModel)]="nuevoUsuario.nombre" name="nombre" required />
            </label>
            <label>
              Email
              <input type="email" [(ngModel)]="nuevoUsuario.email" name="email" required />
            </label>
            <label>
              Contraseña
              <input type="password" [(ngModel)]="nuevoUsuario.password" name="password" minlength="8" required />
            </label>
            <label>
              Teléfono
              <input type="text" [(ngModel)]="nuevoUsuario.telefono" name="telefono" />
            </label>
            <label>
              Rol
              <select [(ngModel)]="nuevoUsuario.rol" name="rol" required>
                <option value="" disabled>Seleccionar</option>
                <option *ngFor="let rol of roles" [value]="rol">{{ rol }}</option>
              </select>
            </label>
            <label class="checkbox">
              <input type="checkbox" [(ngModel)]="nuevoUsuario.activo" name="activo" /> Activo
            </label>

            <div *ngIf="mensajeCrear()" class="success">{{ mensajeCrear() }}</div>
            <div *ngIf="errorCrear()" class="error">{{ errorCrear() }}</div>

            <button type="submit" class="btn" [disabled]="creando()">{{ creando() ? 'Creando...' : 'Crear usuario' }}</button>
          </form>
        </div>

        <div class="section">
          <h3>Editar usuario seleccionado</h3>
          <div *ngIf="!usuarioSeleccionado()" class="info">Selecciona un usuario del listado para editarlo.</div>
          <form *ngIf="usuarioSeleccionado()" (ngSubmit)="actualizarUsuario()" class="form">
            <p><strong>ID:</strong> {{ usuarioSeleccionado()?.id }} · {{ usuarioSeleccionado()?.email }}</p>
            <label>
              Nombre
              <input type="text" [(ngModel)]="edicion.nombre" name="editNombre" />
            </label>
            <label>
              Teléfono
              <input type="text" [(ngModel)]="edicion.telefono" name="editTelefono" />
            </label>
            <label>
              Rol
              <select [(ngModel)]="edicion.rol" name="editRol">
                <option value="" disabled>Seleccionar</option>
                <option *ngFor="let rol of roles" [value]="rol">{{ rol }}</option>
              </select>
            </label>

            <div *ngIf="mensajeEditar()" class="success">{{ mensajeEditar() }}</div>
            <div *ngIf="errorEditar()" class="error">{{ errorEditar() }}</div>

            <div class="form-actions">
              <button type="submit" class="btn" [disabled]="actualizando()">{{ actualizando() ? 'Actualizando...' : 'Guardar cambios' }}</button>
              <button type="button" class="btn secondary" (click)="limpiarSeleccion()">Cancelar</button>
            </div>
          </form>
        </div>
      </div>
    </section>
  `,
  styles: [
    `
      .usuarios { display: flex; flex-direction: column; gap: 1rem; }
      .section { background: #ffffff; padding: 1rem; border-radius: 12px; box-shadow: 0 6px 24px rgba(15, 23, 42, 0.07); }
      .section-header { display: flex; justify-content: space-between; align-items: center; gap: 1rem; flex-wrap: wrap; }
      .section-actions { display: flex; gap: 0.5rem; flex-wrap: wrap; }
      .section-actions input { padding: 0.5rem 0.75rem; border-radius: 8px; border: 1px solid #d1d5db; min-width: 220px; }
      .table-wrapper { overflow-x: auto; }
      table { width: 100%; border-collapse: collapse; }
      th, td { padding: 0.6rem 0.75rem; border-bottom: 1px solid #e5e7eb; text-align: left; }
      th { background: #f9fafb; font-size: 0.9rem; }
      tr.inactive { opacity: 0.7; }
      .badge { padding: 0.2rem 0.55rem; border-radius: 999px; font-size: 0.8rem; font-weight: 600; }
      .badge-active { background: #def7ec; color: #03543f; }
      .badge-inactive { background: #fee2e2; color: #991b1b; }
      .btn { background: #4f46e5; color: white; border: none; border-radius: 8px; padding: 0.5rem 0.9rem; cursor: pointer; font-weight: 600; }
      .btn.secondary { background: transparent; border: 1px solid #c4b5fd; color: #4c1d95; }
      .btn.small { padding: 0.35rem 0.7rem; font-size: 0.8rem; }
      .form { display: flex; flex-direction: column; gap: 0.75rem; }
      .form label { display: flex; flex-direction: column; font-weight: 600; color: #111827; gap: 0.35rem; }
      .form input, .form select { padding: 0.55rem 0.7rem; border-radius: 8px; border: 1px solid #d1d5db; }
      .form-actions { display: flex; gap: 0.5rem; flex-wrap: wrap; }
      .checkbox { flex-direction: row; align-items: center; gap: 0.45rem; font-weight: 500; }
      .error { color: #b91c1c; background: #fee2e2; padding: 0.5rem; border-radius: 8px; }
      .success { color: #065f46; background: #d1fae5; padding: 0.5rem; border-radius: 8px; }
      .info { color: #334155; background: #e2e8f0; padding: 0.5rem; border-radius: 8px; }
      .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 1rem; }
      @media (max-width: 600px) { .section-actions { flex-direction: column; align-items: stretch; } }
    `
  ]
})
export class UsuariosDashboardComponent implements OnInit {
  private readonly usuarios = signal<Usuario[]>([]);
  private readonly seleccionado = signal<Usuario | null>(null);

  filtro = '';
  roles: Usuario['rol'][] = ['ADMIN', 'ORGANIZADOR', 'PROVEEDOR'];

  cargandoLista = signal(false);
  errorLista = signal<string | null>(null);

  creando = signal(false);
  mensajeCrear = signal<string | null>(null);
  errorCrear = signal<string | null>(null);

  actualizando = signal(false);
  mensajeEditar = signal<string | null>(null);
  errorEditar = signal<string | null>(null);
  cargandoToggleId = signal<number | null>(null);

  nuevoUsuario: UsuarioCreateRequest = {
    nombre: '',
    email: '',
    password: '',
    telefono: '',
    rol: 'ORGANIZADOR',
    activo: true
  };

  edicion: UsuarioUpdateRequest = {};

  usuariosFiltrados = computed(() => {
    const term = this.filtro.trim().toLowerCase();
    if (!term) {
      return this.usuarios();
    }
    return this.usuarios().filter((u) =>
      u.nombre?.toLowerCase().includes(term) ||
      u.email?.toLowerCase().includes(term)
    );
  });

  usuarioSeleccionado = computed(() => this.seleccionado());

  constructor(private readonly service: UsuariosService) {}

  ngOnInit(): void {
    this.refrescar();
  }

  refrescar() {
    this.cargandoLista.set(true);
    this.errorLista.set(null);
    this.service.listar().subscribe({
      next: (resp) => {
        this.usuarios.set(resp);
        this.cargandoLista.set(false);
      },
      error: (err) => {
        console.error('[UsuariosDashboard] Error listando usuarios', err);
        this.errorLista.set(err?.error?.message || err?.message || 'Error al listar usuarios');
        this.cargandoLista.set(false);
      }
    });
  }

  crearUsuario() {
    if (!this.nuevoUsuario.nombre || !this.nuevoUsuario.email || !this.nuevoUsuario.password || !this.nuevoUsuario.rol) {
      this.errorCrear.set('Completa los campos obligatorios.');
      return;
    }

    this.creando.set(true);
    this.errorCrear.set(null);
    this.mensajeCrear.set(null);

    this.service.crear(this.nuevoUsuario).subscribe({
      next: (usuario) => {
        this.mensajeCrear.set(`Usuario creado (ID ${usuario.id}).`);
        this.usuarios.set([usuario, ...this.usuarios()]);
        this.nuevoUsuario = {
          nombre: '',
          email: '',
          password: '',
          telefono: '',
          rol: 'ORGANIZADOR',
          activo: true
        };
        this.creando.set(false);
      },
      error: (err) => {
        console.error('[UsuariosDashboard] Error creando usuario', err);
        this.errorCrear.set(err?.error?.message || err?.message || 'No se pudo crear el usuario');
        this.creando.set(false);
      }
    });
  }

  seleccionar(usuario: Usuario) {
    this.seleccionado.set(usuario);
    this.edicion = {
      nombre: usuario.nombre,
      telefono: usuario.telefono,
      rol: usuario.rol
    };
  }

  limpiarSeleccion() {
    this.seleccionado.set(null);
    this.edicion = {};
    this.mensajeEditar.set(null);
    this.errorEditar.set(null);
  }

  actualizarUsuario() {
    const current = this.seleccionado();
    if (!current) {
      this.errorEditar.set('Selecciona un usuario.');
      return;
    }

    if (!this.edicion.nombre && !this.edicion.telefono && !this.edicion.rol) {
      this.errorEditar.set('Ingresa al menos un campo para actualizar.');
      return;
    }

    this.actualizando.set(true);
    this.mensajeEditar.set(null);
    this.errorEditar.set(null);

    this.service.actualizarParcial(current.id, this.edicion).subscribe({
      next: (usuario) => {
        this.mensajeEditar.set('Usuario actualizado.');
        this.actualizando.set(false);
        this.usuarios.set(this.usuarios().map((u) => (u.id === usuario.id ? usuario : u)));
        this.seleccionado.set(usuario);
      },
      error: (err) => {
        console.error('[UsuariosDashboard] Error actualizando', err);
        this.errorEditar.set(err?.error?.message || err?.message || 'No se pudo actualizar');
        this.actualizando.set(false);
      }
    });
  }

  alternarEstado(usuario: Usuario) {
    this.cargandoToggleId.set(usuario.id);

    if (usuario.activo) {
      this.service.desactivar(usuario.id).subscribe({
        next: () => {
          this.actualizarEnArreglo({ ...usuario, activo: false });
          this.cargandoToggleId.set(null);
        },
        error: (err) => {
          console.error('[UsuariosDashboard] Error desactivando', err);
          this.errorLista.set(err?.error?.message || err?.message || 'No se pudo desactivar');
          this.cargandoToggleId.set(null);
        }
      });
      return;
    }

    this.service.activar(usuario.id).subscribe({
      next: (resp) => {
        this.actualizarEnArreglo(resp);
        this.cargandoToggleId.set(null);
      },
      error: (err) => {
        console.error('[UsuariosDashboard] Error activando', err);
        this.errorLista.set(err?.error?.message || err?.message || 'No se pudo activar');
        this.cargandoToggleId.set(null);
      }
    });
  }

  private actualizarEnArreglo(usuario: Usuario) {
    this.usuarios.set(this.usuarios().map((u) => (u.id === usuario.id ? usuario : u)));
    if (this.seleccionado()?.id === usuario.id) {
      this.seleccionado.set(usuario);
      this.edicion = {
        nombre: usuario.nombre,
        telefono: usuario.telefono,
        rol: usuario.rol
      };
    }
  }
}
