package com.eventos.ofertas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear o actualizar una oferta")
public class OfertaRequestDTO {
    
    @NotNull(message = "El ID del proveedor es obligatorio")
    @Schema(description = "ID del proveedor que ofrece el producto", example = "1", required = true)
    private Long proveedorId;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder 150 caracteres")
    @Schema(description = "Título de la oferta", example = "Servicio de Catering", required = true)
    private String titulo;
    
    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "ID de la categoría a la que pertenece", example = "1", required = true)
    private Long idCategoria;
    
    @Size(max = 400, message = "La descripción no puede exceder 400 caracteres")
    @Schema(description = "Descripción detallada de la oferta", example = "Laptop HP con procesador Intel i5, 8GB RAM, 256GB SSD")
    private String descripcion;
    
    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio base debe ser mayor a 0")
    @Schema(description = "Precio base del producto", example = "899.99", required = true)
    private BigDecimal precioBase;
    
    @Pattern(regexp = "borrador|publicado|archivado", message = "Estado inválido")
    @Schema(description = "Estado de la oferta", example = "publicado", allowableValues = {"borrador", "publicado", "archivado"})
    private String estado;
    
    @Schema(description = "Indica si la oferta está activa", example = "true")
    private Boolean activo;
    
    @Schema(description = "URLs de las imágenes o medios de la oferta", example = "[\"https://example.com/imagen1.jpg\", \"https://example.com/imagen2.jpg\"]")
    private List<String> urlsMedia;
}