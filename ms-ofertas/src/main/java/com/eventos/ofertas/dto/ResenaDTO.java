package com.eventos.ofertas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {
    private Long idResena;
    private Long usuarioId;
    private Short calificacion;
    private String comentario;
    private LocalDateTime fecha;
}