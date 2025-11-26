export interface Oferta {
  id?: number;
  idOferta?: number;
  proveedorId?: number;
  titulo?: string;
  descripcion?: string;
  precioBase?: number;
  estado?: string;
  activo?: boolean;
  categoriaId?: number;   // alias común
  idCategoria?: number;   // backend usa este campo en algunos endpoints
  urlsMedia?: string[];
}
