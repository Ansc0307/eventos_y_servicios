package com.eventos.ofertas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ofertasmedia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaMedia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media")
    private Long idMedia;
    
    @Column(name = "url", columnDefinition = "TEXT")
    private String url;
    
    @ManyToOne
    @JoinColumn(name = "id_ofertas")
    private Oferta oferta;
}