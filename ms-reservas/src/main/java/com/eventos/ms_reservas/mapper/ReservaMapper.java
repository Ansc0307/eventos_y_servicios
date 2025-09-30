package com.eventos.ms_reservas.mapper;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.model.Reserva;
import java.time.LocalDateTime;

public class ReservaMapper {

    public static ReservaDTO toDTO(Reserva reserva) {
        return new ReservaDTO(
            reserva.getIdReserva(),
            reserva.getIdSolicitud(),
            reserva.getFechaReservaInicio(),
            reserva.getFechaReservaFin(),
            reserva.getEstado(),
            reserva.getFechaCreacion(),
            reserva.getFechaActualizacion()
        );
    }

    public static Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        // NO establecer el ID para nuevas entidades - Hibernate lo generará automáticamente
        // reserva.setIdReserva(dto.getIdReserva());
        reserva.setIdSolicitud(dto.getIdSolicitud());
        reserva.setFechaReservaInicio(dto.getFechaReservaInicio());
        reserva.setFechaReservaFin(dto.getFechaReservaFin());
        reserva.setEstado(dto.getEstado());
        
        // Las fechas de auditoría se manejan automáticamente con @PrePersist y @PreUpdate
        // Solo establecer si vienen en el DTO (para casos especiales)
        if (dto.getFechaCreacion() != null) {
            reserva.setFechaCreacion(dto.getFechaCreacion());
        }
        if (dto.getFechaActualizacion() != null) {
            reserva.setFechaActualizacion(dto.getFechaActualizacion());
        }
        
        return reserva;
    }

    public static Reserva toEntityWithId(ReservaDTO dto) {
        Reserva reserva = toEntity(dto);
        reserva.setIdReserva(dto.getIdReserva());
        return reserva;
    }
}
