package com.eventos.ofertas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long idResena;
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Column(name = "calificacion")
    private Short calificacion;
    
    @Column(name = "comentario", length = 180)
    private String comentario;
    
    @CreationTimestamp
    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;
    
    @ManyToOne
    @JoinColumn(name = "id_ofertas")
    private Oferta oferta;
}