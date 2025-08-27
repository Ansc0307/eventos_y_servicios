package com.eventos.ofertas.model;

public class Oferta {
    private String id;
    private String titulo;
    private String descripcion;
    private double precio;
    private String categoria;

    public Oferta(String id, String titulo, String descripcion, double precio, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters
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
}
