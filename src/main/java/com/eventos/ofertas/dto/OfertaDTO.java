package com.eventos.ofertas.dto;

import com.eventos.ofertas.entity.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Schema(name = "OfertaDTO", description = "DTO para crear y exponer ofertas")
public class OfertaDTO {

    @Schema(description = "Identificador de la oferta", example = "1")
    private Long id;

    @NotNull(message = "providerId es obligatorio")
    @Schema(description = "ID del proveedor (usuario)", example = "2")
    private Long providerId;

    @NotBlank
    @Size(min = 5, max = 150)
    @Schema(example = "Salón de Eventos Primavera")
    private String titulo;


    @NotBlank
    @Size(min = 20, max = 2000)
    @Schema(example = "Espacio para 200 personas con catering incluido y estacionamiento.")
    private String descripcion;


    @NotNull
    @Schema(description = "Categoría de la oferta")
    private Categoria categoria;


    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "precioBase debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2)
    @Schema(example = "4500.00")
    private BigDecimal precioBase;


    @Schema(description = "URLs de fotos o videos")
    private List<@Pattern(regexp = "^(https?)://.+$", message = "URL inválida") String> mediaUrls;


    @Schema(description = "Fecha de creación")
    private Instant createdAt;


    @Schema(description = "Fecha de actualización")
    private Instant updatedAt;


    @Schema(description = "Estado de publicación", example = "PUBLICADA")
    private String estado; // DRAFT, PUBLICADA, ARCHIVADA


    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase; }
    public List<String> getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(List<String> mediaUrls) { this.mediaUrls = mediaUrls; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}