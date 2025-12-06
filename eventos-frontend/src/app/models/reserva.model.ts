export interface Reserva {
  idReserva: number;
  idSolicitud: number;
  fechaReservaInicio: string; // ISO string
  fechaReservaFin: string; // ISO string
  estado: string;
  fechaCreacion?: string;
  fechaActualizacion?: string;
}
