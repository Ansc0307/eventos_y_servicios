package com.eventos.ofertas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaResponseDTO {
    private Long idOfertas;
    private Long proveedorId;
    private String titulo;
    private CategoriaDTO categoria;
    private String descripcion;
    private BigDecimal precioBase;
    private String estado;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    private Boolean activo;
    private List<OfertaMediaDTO> medias;
    private List<PrecioDescuentoDTO> descuentos;
    private List<ResenaDTO> resenas;
}