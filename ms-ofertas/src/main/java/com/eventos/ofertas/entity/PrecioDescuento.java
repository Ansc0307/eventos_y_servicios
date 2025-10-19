package com.eventos.ofertas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "preciosdescuentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioDescuento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descuento")
    private Long idDescuento;
    
    @ManyToOne
    @JoinColumn(name = "id_ofertas")
    private Oferta oferta;
    
    @Column(name = "nombre", length = 150)
    private String nombre;
    
    @Column(name = "tipo_descuento", length = 20)
    private String tipoDescuento;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;
}