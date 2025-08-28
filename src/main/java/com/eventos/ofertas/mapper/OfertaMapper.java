package com.eventos.ofertas.mapper;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.entity.Oferta;

public class OfertaMapper {

    public static OfertaDTO toDTO(Oferta oferta) {
        return new OfertaDTO(
            oferta.getId(),
            oferta.getTitulo(),
            oferta.getDescripcion(),
            oferta.getPrecio(),
            oferta.getCategoria()
        );
    }
}
