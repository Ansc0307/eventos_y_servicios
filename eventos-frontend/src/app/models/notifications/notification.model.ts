export interface Notificacion {
  id: number;
  asunto: string;
  mensaje: string;
  fechaCreacion: string;
  leido: boolean;
  userId: number;
  prioridad: PrioridadSimple;
  tipoNotificacion: TipoNotificacionSimple;
}

export interface PrioridadSimple {
  id: number;
  nombre?: string;
}

export interface TipoNotificacionSimple {
  id: number;
  nombre?: string;
}

// Para crear/editar notificaciones
export interface NotificacionCreate {
  asunto: string;
  mensaje: string;
  userId: number;
  prioridad: { id: number };
  tipoNotificacion: { id: number };
}