export interface Solicitud {
  idSolicitud: number;
  fechaSolicitud: string; // ISO string
  estadoSolicitud: string;
  idOrganizador: number;
  idProovedor: number;
  idOferta: number;
}
