package com.eventos.ofertas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de una categoría")
public class CategoriaDTO {
    
    @Schema(description = "ID único de la categoría", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idCategoria;
    
    @NotBlank(message = "El detalle no puede estar vacío")
    @Size(max = 50, message = "El detalle no puede exceder 50 caracteres")
    @Schema(description = "Nombre o descripción de la categoría", example = "Electrónica", required = true)
    private String detalle;
}