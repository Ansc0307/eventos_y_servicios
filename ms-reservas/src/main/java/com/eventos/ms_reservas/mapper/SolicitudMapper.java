package com.eventos.ms_reservas.mapper;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.model.Solicitud;

public class SolicitudMapper {

    public static SolicitudDTO toDTO(Solicitud entidad) {
        if (entidad == null) return null;

        SolicitudDTO dto = new SolicitudDTO();
        dto.setIdSolicitud(entidad.getIdSolicitud());
        dto.setFechaSolicitud(entidad.getFechaSolicitud());
        dto.setEstadoSolicitud(entidad.getEstadoSolicitud());
        dto.setIdOrganizador(entidad.getIdOrganizador());
        dto.setIdProovedor(entidad.getIdProovedor());
        dto.setIdOferta(entidad.getIdOferta());

        return dto;
    }

    public static Solicitud toEntity(SolicitudDTO dto) {
        if (dto == null) return null;

        Solicitud entidad = new Solicitud();
        entidad.setIdSolicitud(dto.getIdSolicitud());
        entidad.setFechaSolicitud(dto.getFechaSolicitud());
        entidad.setEstadoSolicitud(dto.getEstadoSolicitud());
        entidad.setIdOrganizador(dto.getIdOrganizador());
        entidad.setIdProovedor(dto.getIdProovedor());
        entidad.setIdOferta(dto.getIdOferta());

        return entidad;
    }
}