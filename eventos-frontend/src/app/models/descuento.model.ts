export interface Descuento {
  id: number;
  nombre: string;
  tipoDescuento: 'porcentaje' | 'monto';
  valor: number;
  fechaInicio: string;
  fechaFin: string;
}
