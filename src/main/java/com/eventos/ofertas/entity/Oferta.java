package com.eventos.ofertas.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Oferta {
    private Long id;
    private Long providerId;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private BigDecimal precioBase;
    private List<String> mediaUrls;
    private Instant createdAt;
    private Instant updatedAt;
    private String estado; //borrador, publicada, archivada
    private String slug;

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
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
}
