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

// Configuraciones de tipos de notificación
export const TIPOS_NOTIFICACION_CONFIG: Record<string, { 
  icon: string; 
  color: string;
  bgColor: string;
}> = {
  'ALERTA': {
    icon: 'warning',
    color: 'text-red-600',
    bgColor: 'bg-red-50 dark:bg-red-900/20'
  },
  'INFORMATIVA': {
    icon: 'info',
    color: 'text-blue-600',
    bgColor: 'bg-blue-50 dark:bg-blue-900/20'
  },
  'PROMOCION': {
    icon: 'local_offer',
    color: 'text-purple-600',
    bgColor: 'bg-purple-50 dark:bg-purple-900/20'
  },
  'SISTEMA': {
    icon: 'settings',
    color: 'text-slate-600',
    bgColor: 'bg-slate-50 dark:bg-slate-900/20'
  },
  'RECORDATORIO': {
    icon: 'notifications',
    color: 'text-amber-600',
    bgColor: 'bg-amber-50 dark:bg-amber-900/20'
  }
};

// Configuraciones de prioridades
export const PRIORIDADES_CONFIG: Record<string, {
  color: string;
  bgColor: string;
  badgeColor: string;
}> = {
  'ALTA': {
    color: 'text-red-600 dark:text-red-400',
    bgColor: 'bg-red-500/10',
    badgeColor: 'bg-red-500 text-white'
  },
  'MEDIA': {
    color: 'text-yellow-600 dark:text-yellow-400',
    bgColor: 'bg-yellow-500/10',
    badgeColor: 'bg-yellow-500 text-white'
  },
  'BAJA': {
    color: 'text-green-600 dark:text-green-400',
    bgColor: 'bg-green-500/10',
    badgeColor: 'bg-green-500 text-white'
  }
};