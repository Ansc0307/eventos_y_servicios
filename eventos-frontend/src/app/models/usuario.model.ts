export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  telefono?: string;
  rol: 'ADMIN' | 'ORGANIZADOR' | 'PROVEEDOR';
  activo: boolean;
  creadoEn?: string;
  actualizadoEn?: string;
}

export interface UsuarioCreateRequest {
  nombre: string;
  email: string;
  password: string;
  rol: Usuario['rol'];
  telefono?: string;
  activo?: boolean;
}

export interface UsuarioUpdateRequest {
  nombre?: string;
  telefono?: string;
  rol?: Usuario['rol'];
}
