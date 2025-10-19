package com.eventos.ofertas.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un descuento")
public class PrecioDescuentoRequestDTO {
    
    @NotBlank(message = "El nombre del descuento es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    @Schema(description = "Nombre del descuento", example = "Black Friday 2025", required = true)
    private String nombre;
    
    @NotBlank(message = "El tipo de descuento es obligatorio")
    @Pattern(regexp = "porcentaje|monto", message = "Tipo de descuento inválido")
    @Schema(description = "Tipo de descuento", example = "porcentaje", allowableValues = {"porcentaje", "monto"}, required = true)
    private String tipoDescuento;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio del descuento", example = "2025-11-25T00:00:00", required = true)
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    @Schema(description = "Fecha de fin del descuento", example = "2025-11-30T23:59:59", required = true)
    private LocalDateTime fechaFin;
    
    @NotNull(message = "El valor del descuento es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor a 0")
    @Schema(description = "Valor del descuento (porcentaje o monto según tipo)", example = "20.00", required = true)
    private BigDecimal valor;
}