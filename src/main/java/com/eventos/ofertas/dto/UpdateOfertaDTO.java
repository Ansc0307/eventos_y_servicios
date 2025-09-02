package com.eventos.ofertas.dto;

import com.eventos.ofertas.entity.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


import java.math.BigDecimal;
import java.util.List;

@Schema(name = "UpdateOfertaDTO", description = "DTO para actualizar parcialmente una oferta")
public class UpdateOfertaDTO {
@Size(min = 5, max = 120)
private String titulo;
@Size(min = 20, max = 2000)
private String descripcion;
private Categoria categoria;
@DecimalMin(value = "0.0", inclusive = false)
@Digits(integer = 10, fraction = 2)
private BigDecimal precioBase;
private List<@Pattern(regexp = "^(https?)://.+$") String> mediaUrls;
private String estado; // DRAFT, PUBLICADA, ARCHIVADA


// getters y setters
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
public String getEstado() { return estado; }
public void setEstado(String estado) { this.estado = estado; }
}
