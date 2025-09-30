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
        reserva.setIdReserva(dto.getIdReserva());
        reserva.setIdSolicitud(dto.getIdSolicitud());
        reserva.setFechaReservaInicio(dto.getFechaReservaInicio());
        reserva.setFechaReservaFin(dto.getFechaReservaFin());
        reserva.setEstado(dto.getEstado());
        reserva.setFechaCreacion(dto.getFechaCreacion());
        reserva.setFechaActualizacion(dto.getFechaActualizacion());
        
        // Si no se proporciona fecha de creación, usar la actual
        if (reserva.getFechaCreacion() == null) {
            reserva.setFechaCreacion(LocalDateTime.now());
        }
        
        // Siempre actualizar la fecha de actualización
        reserva.setFechaActualizacion(LocalDateTime.now());
        
        return reserva;
    }
}
