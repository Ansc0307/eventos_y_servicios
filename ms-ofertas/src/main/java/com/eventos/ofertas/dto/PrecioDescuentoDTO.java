package com.eventos.ofertas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioDescuentoDTO {
    private Long idDescuento;
    private String nombre;
    private String tipoDescuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal valor;
}