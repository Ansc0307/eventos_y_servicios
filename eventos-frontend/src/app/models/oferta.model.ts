export interface Oferta {
  id: number;
  proveedorId: number;
  titulo: string;
  idCategoria: number;
  descripcion: string;
  precioBase: number;
  estado: string;
  activo: boolean;
  urlsMedia: string[];
}
