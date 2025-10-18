package com.eventos.ofertas;

import com.eventos.ofertas.controller.CategoriaController;
import com.eventos.ofertas.dto.CategoriaDTO;
import com.eventos.ofertas.service.CategoriaService;
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
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        categoriaDTO = new CategoriaDTO();
        // Aquí configurarías los datos necesarios para categoriaDTO
    }

    @Test
    void crearCategoria_DeberiaRetornarCategoriaYEstado201() {
        // Arrange
        when(categoriaService.crearCategoria(any(CategoriaDTO.class))).thenReturn(categoriaDTO);

        // Act
        ResponseEntity<CategoriaDTO> response = categoriaController.crearCategoria(categoriaDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoriaDTO, response.getBody());
        verify(categoriaService).crearCategoria(categoriaDTO);
    }

    @Test
    void obtenerTodasLasCategorias_DeberiaRetornarListaYEstado200() {
        // Arrange
        List<CategoriaDTO> categorias = Arrays.asList(categoriaDTO);
        when(categoriaService.obtenerTodasLasCategorias()).thenReturn(categorias);

        // Act
        ResponseEntity<List<CategoriaDTO>> response = categoriaController.obtenerTodasLasCategorias();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categorias, response.getBody());
        verify(categoriaService).obtenerTodasLasCategorias();
    }

    @Test
    void obtenerCategoriaPorId_DeberiaRetornarCategoriaYEstado200() {
        // Arrange
        Long categoriaId = 1L;
        when(categoriaService.obtenerCategoriaPorId(categoriaId)).thenReturn(categoriaDTO);

        // Act
        ResponseEntity<CategoriaDTO> response = categoriaController.obtenerCategoriaPorId(categoriaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoriaDTO, response.getBody());
        verify(categoriaService).obtenerCategoriaPorId(categoriaId);
    }

    @Test
    void actualizarCategoria_DeberiaRetornarCategoriaActualizadaYEstado200() {
        // Arrange
        Long categoriaId = 1L;
        when(categoriaService.actualizarCategoria(eq(categoriaId), any(CategoriaDTO.class))).thenReturn(categoriaDTO);

        // Act
        ResponseEntity<CategoriaDTO> response = categoriaController.actualizarCategoria(categoriaId, categoriaDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoriaDTO, response.getBody());
        verify(categoriaService).actualizarCategoria(categoriaId, categoriaDTO);
    }

    @Test
    void eliminarCategoria_DeberiaRetornarEstado204() {
        // Arrange
        Long categoriaId = 1L;
        doNothing().when(categoriaService).eliminarCategoria(categoriaId);

        // Act
        ResponseEntity<Void> response = categoriaController.eliminarCategoria(categoriaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoriaService).eliminarCategoria(categoriaId);
    }
}