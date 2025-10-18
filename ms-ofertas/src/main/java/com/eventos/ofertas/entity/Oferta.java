package com.eventos.ofertas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ofertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oferta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ofertas")
    private Long idOfertas;
    
    @Column(name = "proveedor_id")
    private Long proveedorId;
    
    @Column(name = "titulo", length = 150)
    private String titulo;
    
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    
    @Column(name = "descripcion", length = 400)
    private String descripcion;
    
    @Column(name = "precio_base", precision = 10, scale = 2)
    private BigDecimal precioBase;
    
    @Column(name = "estado", length = 20, columnDefinition = "varchar(20) default 'borrador'")
    private String estado = "borrador";
    
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
    
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
    
    @Column(name = "activo", columnDefinition = "boolean default true")
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfertaMedia> medias;
    
    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrecioDescuento> descuentos;
    
    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resena> resenas;
}