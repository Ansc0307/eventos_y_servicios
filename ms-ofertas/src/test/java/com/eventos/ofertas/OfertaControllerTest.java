package com.eventos.ofertas;

import com.eventos.ofertas.controller.OfertaController;
import com.eventos.ofertas.dto.OfertaRequestDTO;
import com.eventos.ofertas.dto.OfertaResponseDTO;
import com.eventos.ofertas.dto.PrecioDescuentoDTO;
import com.eventos.ofertas.dto.PrecioDescuentoRequestDTO;
import com.eventos.ofertas.service.OfertaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfertaControllerTest {

    @Mock
    private OfertaService ofertaService;

    @InjectMocks
    private OfertaController ofertaController;

    private OfertaRequestDTO ofertaRequest;
    private OfertaResponseDTO ofertaResponse;
    private PrecioDescuentoRequestDTO descuentoRequest;
    private PrecioDescuentoDTO descuentoResponse;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        ofertaRequest = new OfertaRequestDTO();
        // Aquí configurarías los datos necesarios para ofertaRequest

        ofertaResponse = new OfertaResponseDTO();
        // Aquí configurarías los datos necesarios para ofertaResponse

        descuentoRequest = new PrecioDescuentoRequestDTO();
        // Aquí configurarías los datos necesarios para descuentoRequest

        descuentoResponse = new PrecioDescuentoDTO();
        // Aquí configurarías los datos necesarios para descuentoResponse
    }

    @Test
    void crearOferta_DeberiaRetornarOfertaYEstado201() {
        // Arrange
        when(ofertaService.crearOferta(any(OfertaRequestDTO.class))).thenReturn(ofertaResponse);

        // Act
        ResponseEntity<OfertaResponseDTO> response = ofertaController.crearOferta(ofertaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ofertaResponse, response.getBody());
        verify(ofertaService).crearOferta(ofertaRequest);
    }

    @Test
    void obtenerTodasLasOfertas_DeberiaRetornarListaYEstado200() {
        // Arrange
        List<OfertaResponseDTO> ofertas = Arrays.asList(ofertaResponse);
        when(ofertaService.obtenerTodasLasOfertasActivas()).thenReturn(ofertas);

        // Act
        ResponseEntity<List<OfertaResponseDTO>> response = ofertaController.obtenerTodasLasOfertas();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ofertas, response.getBody());
        verify(ofertaService).obtenerTodasLasOfertasActivas();
    }

    @Test
    void obtenerOfertasPorCategoria_DeberiaRetornarListaYEstado200() {
        // Arrange
        Long categoriaId = 1L;
        List<OfertaResponseDTO> ofertas = Arrays.asList(ofertaResponse);
        when(ofertaService.obtenerOfertasPorCategoria(categoriaId)).thenReturn(ofertas);

        // Act
        ResponseEntity<List<OfertaResponseDTO>> response = ofertaController.obtenerOfertasPorCategoria(categoriaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ofertas, response.getBody());
        verify(ofertaService).obtenerOfertasPorCategoria(categoriaId);
    }

    @Test
    void actualizarOferta_DeberiaRetornarOfertaActualizadaYEstado200() {
        // Arrange
        Long ofertaId = 1L;
        when(ofertaService.actualizarOferta(eq(ofertaId), any(OfertaRequestDTO.class))).thenReturn(ofertaResponse);

        // Act
        ResponseEntity<OfertaResponseDTO> response = ofertaController.actualizarOferta(ofertaId, ofertaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ofertaResponse, response.getBody());
        verify(ofertaService).actualizarOferta(ofertaId, ofertaRequest);
    }

    @Test
    void eliminarOferta_DeberiaRetornarEstado204() {
        // Arrange
        Long ofertaId = 1L;
        doNothing().when(ofertaService).eliminarOferta(ofertaId);

        // Act
        ResponseEntity<Void> response = ofertaController.eliminarOferta(ofertaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ofertaService).eliminarOferta(ofertaId);
    }

    @Test
    void agregarDescuento_DeberiaRetornarDescuentoYEstado201() {
        // Arrange
        Long ofertaId = 1L;
        when(ofertaService.agregarDescuento(eq(ofertaId), any(PrecioDescuentoRequestDTO.class)))
                .thenReturn(descuentoResponse);

        // Act
        ResponseEntity<PrecioDescuentoDTO> response = ofertaController.agregarDescuento(ofertaId, descuentoRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(descuentoResponse, response.getBody());
        verify(ofertaService).agregarDescuento(ofertaId, descuentoRequest);
    }

    @Test
    void eliminarDescuento_DeberiaRetornarEstado204() {
        // Arrange
        Long ofertaId = 1L;
        Long descuentoId = 1L;
        doNothing().when(ofertaService).eliminarDescuento(ofertaId, descuentoId);

        // Act
        ResponseEntity<Void> response = ofertaController.eliminarDescuento(ofertaId, descuentoId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ofertaService).eliminarDescuento(ofertaId, descuentoId);
    }
}
