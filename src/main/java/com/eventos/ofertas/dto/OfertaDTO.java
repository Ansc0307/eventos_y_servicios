package com.eventos.ofertas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de una oferta de espacio o servicio para eventos")
public class OfertaDTO {

    @Schema(description = "Identificador único de la oferta", example = "1")
    private String id;

    @Schema(description = "Título de la oferta", example = "Salón de Eventos Primavera")
    private String titulo;

    @Schema(description = "Descripción detallada de la oferta", example = "Espacio para 200 personas con catering incluido")
    private String descripcion;

    @Schema(description = "Precio de la oferta en dólares", example = "4500.00")
    private double precio;

    @Schema(description = "Categoría de la oferta (Espacio o Servicio)", example = "Espacio")
    private String categoria;

    // Constructor vacío
    public OfertaDTO() {}
    // Constructor completo
    public OfertaDTO(String id, String titulo, String descripcion, double precio, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
