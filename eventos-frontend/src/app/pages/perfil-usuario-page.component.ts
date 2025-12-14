import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Usuario, UsuarioUpdateRequest } from '../models/usuario.model';
import { UsuariosService } from '../services/usuarios.service';

@Component({
  selector: 'app-perfil-usuario-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="w-full max-w-screen-xl mx-auto px-4 sm:px-6 md:px-8 py-6">
      <div class="flex items-start justify-between gap-4 flex-wrap">
        <div>
          <h1 class="text-2xl font-bold text-text-light dark:text-text-dark">Perfil</h1>
          <p class="text-sm text-slate-600 dark:text-slate-300">Información del usuario autenticado</p>
        </div>

        <button
          type="button"
          class="h-10 px-4 rounded-lg bg-primary text-white font-semibold hover:bg-primary/90 disabled:opacity-60"
          (click)="toggleEdicion()"
          [disabled]="cargando() || !usuario()"
        >
          {{ editando() ? 'Cancelar' : 'Editar' }}
        </button>
      </div>

      <div *ngIf="cargando()" class="mt-6 rounded-lg border border-slate-200 dark:border-slate-800 bg-background-light dark:bg-background-dark p-4 text-slate-600 dark:text-slate-300">
        Cargando perfil...
      </div>

      <div *ngIf="error()" class="mt-6 rounded-lg border border-red-200 dark:border-red-900 bg-red-50 dark:bg-red-950/30 p-4 text-red-700 dark:text-red-300">
        {{ error() }}
      </div>

      <div *ngIf="exito()" class="mt-6 rounded-lg border border-emerald-200 dark:border-emerald-900 bg-emerald-50 dark:bg-emerald-950/30 p-4 text-emerald-700 dark:text-emerald-300">
        {{ exito() }}
      </div>

      <div *ngIf="usuario() as u" class="mt-6 grid grid-cols-1 lg:grid-cols-3 gap-4">
        <section class="lg:col-span-2 rounded-xl border border-slate-200 dark:border-slate-800 bg-background-light dark:bg-background-dark">
          <div class="px-6 py-4 border-b border-slate-200 dark:border-slate-800">
            <h2 class="text-lg font-semibold text-text-light dark:text-text-dark">Datos generales</h2>
          </div>

          <div class="p-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div class="rounded-lg border border-slate-200 dark:border-slate-800 p-4">
              <p class="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">Nombre</p>
              <p class="mt-1 text-sm font-semibold text-text-light dark:text-text-dark">{{ u.nombre }}</p>
            </div>
            <div class="rounded-lg border border-slate-200 dark:border-slate-800 p-4">
              <p class="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">Email</p>
              <p class="mt-1 text-sm font-semibold text-text-light dark:text-text-dark">{{ u.email }}</p>
            </div>
            <div class="rounded-lg border border-slate-200 dark:border-slate-800 p-4">
              <p class="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">Teléfono</p>
              <p class="mt-1 text-sm font-semibold text-text-light dark:text-text-dark">{{ u.telefono || '—' }}</p>
            </div>
            <div class="rounded-lg border border-slate-200 dark:border-slate-800 p-4">
              <p class="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">Rol</p>
              <p class="mt-1 text-sm font-semibold text-text-light dark:text-text-dark">{{ u.rol }}</p>
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-slate-200 dark:border-slate-800 bg-background-light dark:bg-background-dark">
          <div class="px-6 py-4 border-b border-slate-200 dark:border-slate-800">
            <h2 class="text-lg font-semibold text-text-light dark:text-text-dark">Editar perfil</h2>
          </div>

          <div class="p-6">
            <div *ngIf="!editando()" class="text-sm text-slate-600 dark:text-slate-300">
              Presiona <span class="font-semibold">Editar</span> para modificar tus datos.
            </div>

            <form *ngIf="editando()" [formGroup]="form" (ngSubmit)="guardar()" class="space-y-4">
              <label class="block">
                <span class="text-sm font-semibold text-text-light dark:text-text-dark">Nombre</span>
                <input
                  class="mt-1 w-full h-10 px-3 rounded-lg border border-slate-200 dark:border-slate-800 bg-white/70 dark:bg-slate-900/30 text-text-light dark:text-text-dark"
                  type="text"
                  formControlName="nombre"
                />
              </label>

              <label class="block">
                <span class="text-sm font-semibold text-text-light dark:text-text-dark">Teléfono</span>
                <input
                  class="mt-1 w-full h-10 px-3 rounded-lg border border-slate-200 dark:border-slate-800 bg-white/70 dark:bg-slate-900/30 text-text-light dark:text-text-dark"
                  type="text"
                  formControlName="telefono"
                />
              </label>

              <button
                type="submit"
                class="w-full h-10 rounded-lg bg-primary text-white font-semibold hover:bg-primary/90 disabled:opacity-60"
                [disabled]="guardando() || form.invalid"
              >
                {{ guardando() ? 'Guardando...' : 'Guardar cambios' }}
              </button>
            </form>
          </div>
        </section>
      </div>
    </div>
  `
})
export class PerfilUsuarioPageComponent implements OnInit {
  private readonly usuariosService = inject(UsuariosService);
  private readonly fb = inject(FormBuilder);

  cargando = signal(false);
  guardando = signal(false);
  editando = signal(false);
  error = signal<string | null>(null);
  exito = signal<string | null>(null);

  usuario = signal<Usuario | null>(null);

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    telefono: ['']
  });

  constructor() {}

  ngOnInit(): void {
    this.cargarPerfil();
  }

  toggleEdicion(): void {
    this.exito.set(null);
    this.error.set(null);
    this.editando.set(!this.editando());

    const u = this.usuario();
    if (this.editando() && u) {
      this.form.reset({
        nombre: u.nombre ?? '',
        telefono: u.telefono ?? ''
      });
    }
  }

  cargarPerfil(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.exito.set(null);
    this.usuariosService.me().subscribe({
      next: (u) => {
        this.usuario.set(u);
        this.form.reset({
          nombre: u.nombre ?? '',
          telefono: u.telefono ?? ''
        });
        this.cargando.set(false);
      },
      error: (err) => {
        console.error('[Perfil] Error cargando /usuarios/me', err);
        this.error.set(err?.error?.message || err?.message || 'No se pudo cargar el perfil');
        this.cargando.set(false);
      }
    });
  }

  guardar(): void {
    const u = this.usuario();
    if (!u) {
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: UsuarioUpdateRequest = {
      nombre: this.form.value.nombre ?? undefined,
      telefono: this.form.value.telefono ?? undefined
    };

    this.guardando.set(true);
    this.error.set(null);
    this.exito.set(null);

    this.usuariosService.actualizarMiPerfil(payload).subscribe({
      next: (updated) => {
        this.usuario.set(updated);
        this.editando.set(false);
        this.exito.set('Perfil actualizado.');
        this.guardando.set(false);
      },
      error: (err) => {
        console.error('[Perfil] Error actualizando usuario', err);
        this.error.set(err?.error?.message || err?.message || 'No se pudo actualizar el perfil');
        this.guardando.set(false);
      }
    });
  }
}
