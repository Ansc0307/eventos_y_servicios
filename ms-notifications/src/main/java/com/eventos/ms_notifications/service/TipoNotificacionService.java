package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoNotificacionService {

    private final TipoNotificacionRepository tipoNotificacionRepository;

    public TipoNotificacionService(TipoNotificacionRepository tipoNotificacionRepository) {
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }

    public TipoNotificacionDTO crearTipo(TipoNotificacionDTO dto) {
        TipoNotificacion tipo = new TipoNotificacion(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getRequiereAck(),
                dto.getIcono(),
                dto.getActivo()
        );
        TipoNotificacion saved = tipoNotificacionRepository.save(tipo);
        dto.setId(saved.getId());
        return dto;
    }

    public List<TipoNotificacionDTO> listarTipos() {
        return tipoNotificacionRepository.findAll()
                .stream()
                .map(t -> {
                    TipoNotificacionDTO dto = new TipoNotificacionDTO();
                    dto.setId(t.getId());
                    dto.setNombre(t.getNombre());
                    dto.setDescripcion(t.getDescripcion());
                    dto.setRequiereAck(t.getRequiereAck());
                    dto.setIcono(t.getIcono());
                    dto.setActivo(t.getActivo());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
