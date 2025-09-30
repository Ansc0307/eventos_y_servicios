package com.eventos.ms_reservas.service;

import com.eventos.ms_reservas.config.TestContainerConfig;
import com.eventos.ms_reservas.model.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestContainerConfig.class)
@ActiveProfiles("test")
@Transactional
class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    private Reserva reserva1;
    private Reserva reserva2;

    @BeforeEach
    void setUp() {
        reserva1 = new Reserva();
        reserva1.setIdSolicitud(100);
        reserva1.setFechaReservaInicio(LocalDateTime.of(2024, 1, 15, 10, 0));
        reserva1.setFechaReservaFin(LocalDateTime.of(2024, 1, 15, 12, 0));
        reserva1.setEstado("CONFIRMADA");

        reserva2 = new Reserva();
        reserva2.setIdSolicitud(200);
        reserva2.setFechaReservaInicio(LocalDateTime.of(2024, 1, 16, 14, 0));
        reserva2.setFechaReservaFin(LocalDateTime.of(2024, 1, 16, 16, 0));
        reserva2.setEstado("PENDIENTE");
    }

    @Test
    void shouldSaveReserva() {
        // When
        Reserva savedReserva = reservaService.save(reserva1);

        // Then
        assertThat(savedReserva.getIdReserva()).isNotNull();
        assertThat(savedReserva.getFechaCreacion()).isNotNull();
        assertThat(savedReserva.getFechaActualizacion()).isNotNull();
        assertThat(savedReserva.getIdSolicitud()).isEqualTo(100);
        assertThat(savedReserva.getEstado()).isEqualTo("CONFIRMADA");
    }

    @Test
    void shouldGetReservaById() {
        // Given
        Reserva savedReserva = reservaService.save(reserva1);

        // When
        Reserva foundReserva = reservaService.getById(savedReserva.getIdReserva());

        // Then
        assertThat(foundReserva).isNotNull();
        assertThat(foundReserva.getIdSolicitud()).isEqualTo(100);
        assertThat(foundReserva.getEstado()).isEqualTo("CONFIRMADA");
    }

    @Test
    void shouldReturnNullWhenReservaNotFound() {
        // When
        Reserva foundReserva = reservaService.getById(999);

        // Then
        assertThat(foundReserva).isNull();
    }

    @Test
    void shouldGetAllReservas() {
        // Given
        reservaService.save(reserva1);
        reservaService.save(reserva2);

        // When
        List<Reserva> allReservas = reservaService.getAll();

        // Then
        assertThat(allReservas).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldGetReservasByEstado() {
        // Given
        reservaService.save(reserva1);
        reservaService.save(reserva2);

        // When
        List<Reserva> confirmadas = reservaService.getByEstado("CONFIRMADA");
        List<Reserva> pendientes = reservaService.getByEstado("PENDIENTE");

        // Then
        assertThat(confirmadas).hasSizeGreaterThanOrEqualTo(1);
        assertThat(pendientes).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldUpdateReserva() {
        // Given
        Reserva savedReserva = reservaService.save(reserva1);
        Integer reservaId = savedReserva.getIdReserva();

        // When
        Reserva updatedReserva = new Reserva();
        updatedReserva.setIdSolicitud(101);
        updatedReserva.setFechaReservaInicio(LocalDateTime.of(2024, 1, 20, 9, 0));
        updatedReserva.setFechaReservaFin(LocalDateTime.of(2024, 1, 20, 11, 0));
        updatedReserva.setEstado("CANCELADA");

        Reserva result = reservaService.update(reservaId, updatedReserva);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdReserva()).isEqualTo(reservaId);
        assertThat(result.getIdSolicitud()).isEqualTo(101);
        assertThat(result.getEstado()).isEqualTo("CANCELADA");
        assertThat(result.getFechaCreacion()).isEqualTo(savedReserva.getFechaCreacion()); // No debe cambiar
        assertThat(result.getFechaActualizacion()).isAfterOrEqualTo(savedReserva.getFechaActualizacion()); // Debe actualizarse o ser igual
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentReserva() {
        // When
        Reserva result = reservaService.update(999, reserva1);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldDeleteReserva() {
        // Given
        Reserva savedReserva = reservaService.save(reserva1);
        Integer reservaId = savedReserva.getIdReserva();

        // When
        boolean deleted = reservaService.delete(reservaId);

        // Then
        assertThat(deleted).isTrue();
        assertThat(reservaService.getById(reservaId)).isNull();
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentReserva() {
        // When
        boolean deleted = reservaService.delete(999);

        // Then
        assertThat(deleted).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenSavingConflictingReserva() {
        // Given
        reservaService.save(reserva1); // 10:00 - 12:00

        // When & Then - Intentar guardar una reserva que se solapa (11:00 - 13:00)
        Reserva conflictingReserva = new Reserva();
        conflictingReserva.setIdSolicitud(300);
        conflictingReserva.setFechaReservaInicio(LocalDateTime.of(2024, 1, 15, 11, 0));
        conflictingReserva.setFechaReservaFin(LocalDateTime.of(2024, 1, 15, 13, 0));
        conflictingReserva.setEstado("PENDIENTE");

        assertThatThrownBy(() -> reservaService.save(conflictingReserva))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("conflicto de horarios");
    }

    @Test
    void shouldGetReservasEnRango() {
        // Given
        reservaService.save(reserva1); // 15 enero 10:00
        reservaService.save(reserva2); // 16 enero 14:00

        // When
        LocalDateTime rangoInicio = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime rangoFin = LocalDateTime.of(2024, 1, 15, 23, 59);
        List<Reserva> reservasEnRango = reservaService.getReservasEnRango(rangoInicio, rangoFin);

        // Then
        assertThat(reservasEnRango).hasSizeGreaterThanOrEqualTo(1);
        assertThat(reservasEnRango.stream().anyMatch(r -> r.getIdSolicitud().equals(100))).isTrue();
    }
}