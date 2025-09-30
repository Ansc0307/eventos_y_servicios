package com.eventos.ms_reservas.repository;

import com.eventos.ms_reservas.config.TestContainerConfig;
import com.eventos.ms_reservas.model.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    private Reserva reserva1;
    private Reserva reserva2;

    @BeforeEach
    void setUp() {
        reservaRepository.deleteAll();

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
    void shouldSaveAndFindReserva() {
        // When
        Reserva savedReserva = reservaRepository.save(reserva1);

        // Then
        assertThat(savedReserva.getIdReserva()).isNotNull();
        assertThat(savedReserva.getFechaCreacion()).isNotNull();
        assertThat(savedReserva.getFechaActualizacion()).isNotNull();

        Optional<Reserva> foundReserva = reservaRepository.findById(savedReserva.getIdReserva());
        assertThat(foundReserva).isPresent();
        assertThat(foundReserva.get().getIdSolicitud()).isEqualTo(100);
        assertThat(foundReserva.get().getEstado()).isEqualTo("CONFIRMADA");
    }

    @Test
    void shouldFindByEstado() {
        // Given
        reservaRepository.save(reserva1);
        reservaRepository.save(reserva2);

        // When
        List<Reserva> confirmadas = reservaRepository.findByEstado("CONFIRMADA");
        List<Reserva> pendientes = reservaRepository.findByEstado("PENDIENTE");

        // Then
        assertThat(confirmadas).hasSize(1);
        assertThat(confirmadas.get(0).getIdSolicitud()).isEqualTo(100);

        assertThat(pendientes).hasSize(1);
        assertThat(pendientes.get(0).getIdSolicitud()).isEqualTo(200);
    }

    @Test
    void shouldFindByIdSolicitud() {
        // Given
        reservaRepository.save(reserva1);
        reservaRepository.save(reserva2);

        // When
        List<Reserva> reservasSolicitud100 = reservaRepository.findByIdSolicitud(100);
        List<Reserva> reservasSolicitud200 = reservaRepository.findByIdSolicitud(200);

        // Then
        assertThat(reservasSolicitud100).hasSize(1);
        assertThat(reservasSolicitud100.get(0).getEstado()).isEqualTo("CONFIRMADA");

        assertThat(reservasSolicitud200).hasSize(1);
        assertThat(reservasSolicitud200.get(0).getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    void shouldFindReservasConflictivas() {
        // Given
        reservaRepository.save(reserva1); // 10:00 - 12:00

        // When - Buscar conflictos con una reserva que se solapa (11:00 - 13:00)
        LocalDateTime inicioConflicto = LocalDateTime.of(2024, 1, 15, 11, 0);
        LocalDateTime finConflicto = LocalDateTime.of(2024, 1, 15, 13, 0);
        List<Reserva> conflictivas = reservaRepository.findReservasConflictivas(inicioConflicto, finConflicto);

        // Then
        assertThat(conflictivas).hasSize(1);
        assertThat(conflictivas.get(0).getIdSolicitud()).isEqualTo(100);
    }

    @Test
    void shouldNotFindConflictsWhenNoOverlap() {
        // Given
        reservaRepository.save(reserva1); // 10:00 - 12:00

        // When - Buscar conflictos con una reserva que no se solapa (13:00 - 15:00)
        LocalDateTime inicioSinConflicto = LocalDateTime.of(2024, 1, 15, 13, 0);
        LocalDateTime finSinConflicto = LocalDateTime.of(2024, 1, 15, 15, 0);
        List<Reserva> conflictivas = reservaRepository.findReservasConflictivas(inicioSinConflicto, finSinConflicto);

        // Then
        assertThat(conflictivas).isEmpty();
    }

    @Test
    void shouldFindByFechaReservaInicioBetween() {
        // Given
        reservaRepository.save(reserva1); // 15 enero 10:00
        reservaRepository.save(reserva2); // 16 enero 14:00

        // When
        LocalDateTime rangoInicio = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime rangoFin = LocalDateTime.of(2024, 1, 15, 23, 59);
        List<Reserva> reservasEnRango = reservaRepository.findByFechaReservaInicioBetween(rangoInicio, rangoFin);

        // Then
        assertThat(reservasEnRango).hasSize(1);
        assertThat(reservasEnRango.get(0).getIdSolicitud()).isEqualTo(100);
    }
}