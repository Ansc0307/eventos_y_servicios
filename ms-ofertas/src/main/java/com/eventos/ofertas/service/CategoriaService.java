package com.eventos.ofertas.service;

import com.eventos.ofertas.dto.CategoriaDTO;
import com.eventos.ofertas.entity.Categoria;
import com.eventos.ofertas.exception.BadRequestException;
import com.eventos.ofertas.exception.ResourceNotFoundException;
import com.eventos.ofertas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        // VALIDACIÓN: Verificar si ya existe una categoría con el mismo detalle.
        if (categoriaRepository.existsByDetalle(categoriaDTO.getDetalle())) {
            throw new BadRequestException("Ya existe una categoría con el detalle: " + categoriaDTO.getDetalle());
        }

        Categoria categoria = new Categoria();
        categoria.setDetalle(categoriaDTO.getDetalle());

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirADTO(categoriaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return convertirADTO(categoria);
    }

    @Transactional
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        // VALIDACIÓN: Verificar si el nuevo detalle ya está en uso por OTRA categoría.
        categoriaRepository.findByDetalle(categoriaDTO.getDetalle()).ifPresent(cat -> {
            if (!cat.getIdCategoria().equals(id)) {
                throw new BadRequestException("El detalle '" + categoriaDTO.getDetalle() + "' ya está en uso por otra categoría.");
            }
        });

        categoria.setDetalle(categoriaDTO.getDetalle());
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return convertirADTO(categoriaActualizada);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        // Verificamos que la categoría exista antes de intentar eliminarla.
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
        }

        try {
            categoriaRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            // VALIDACIÓN: Esto ocurre si intentas eliminar una categoría que está siendo usada por otra tabla (ej. en una oferta).
            throw new BadRequestException("No se puede eliminar la categoría con ID " + id + " porque está en uso.");
        }
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setDetalle(categoria.getDetalle());
        return dto;
    }
}