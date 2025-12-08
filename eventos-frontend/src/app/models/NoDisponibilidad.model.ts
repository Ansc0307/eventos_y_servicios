export interface NoDisponibilidad {
  idNoDisponibilidad: number;
  idOferta: number;
  motivo: string;
  fechaInicio: string; // ISO string
  fechaFin: string; // ISO string
  idReserva?: number;
}
