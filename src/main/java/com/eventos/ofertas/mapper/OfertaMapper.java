package com.eventos.ofertas.mapper;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.dto.UpdateOfertaDTO;
import com.eventos.ofertas.entity.Oferta;
import java.time.Instant;

public class OfertaMapper {
    public static Oferta toEntity(OfertaDTO dto) {
        Oferta o = new Oferta();
        o.setId(dto.getId());
        o.setProviderId(dto.getProviderId());
        o.setTitulo(dto.getTitulo());
        o.setDescripcion(dto.getDescripcion());
        o.setCategoria(dto.getCategoria());
        o.setPrecioBase(dto.getPrecioBase());
        o.setMediaUrls(dto.getMediaUrls());
        o.setEstado(dto.getEstado() != null ? dto.getEstado() : "PUBLICADA");
        Instant now = Instant.now();
        o.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : now);
        o.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : now);
        return o;
    }
    public static OfertaDTO toDTO(Oferta o) {
        OfertaDTO dto = new OfertaDTO();
        dto.setId(o.getId());
        dto.setProviderId(o.getProviderId());
        dto.setTitulo(o.getTitulo());
        dto.setDescripcion(o.getDescripcion());
        dto.setCategoria(o.getCategoria());
        dto.setPrecioBase(o.getPrecioBase());
        dto.setMediaUrls(o.getMediaUrls());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setUpdatedAt(o.getUpdatedAt());
        dto.setEstado(o.getEstado());
        return dto;
    }
    public static void applyPatch(Oferta o, UpdateOfertaDTO u) {
        if (u.getTitulo() != null) o.setTitulo(u.getTitulo());
        if (u.getDescripcion() != null) o.setDescripcion(u.getDescripcion());
        if (u.getCategoria() != null) o.setCategoria(u.getCategoria());
        if (u.getPrecioBase() != null) o.setPrecioBase(u.getPrecioBase());
        if (u.getMediaUrls() != null) o.setMediaUrls(u.getMediaUrls());
        if (u.getEstado() != null) o.setEstado(u.getEstado());
        o.setUpdatedAt(Instant.now());
    }
}