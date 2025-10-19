package com.eventos.ofertas.service;

import com.eventos.ofertas.dto.*;
import com.eventos.ofertas.entity.*;
import com.eventos.ofertas.exception.BadRequestException;
import com.eventos.ofertas.exception.ResourceNotFoundException;
import com.eventos.ofertas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfertaService {
    
    private final OfertaRepository ofertaRepository;
    private final CategoriaRepository categoriaRepository;
    private final OfertaMediaRepository ofertaMediaRepository;
    private final PrecioDescuentoRepository precioDescuentoRepository;
    
    @Transactional
    public OfertaResponseDTO crearOferta(OfertaRequestDTO request) {
        // Validar que exista la categoría
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getIdCategoria()));
        
        // Crear la oferta
        Oferta oferta = new Oferta();
        oferta.setProveedorId(request.getProveedorId());
        oferta.setTitulo(request.getTitulo());
        oferta.setCategoria(categoria);
        oferta.setDescripcion(request.getDescripcion());
        oferta.setPrecioBase(request.getPrecioBase());
        oferta.setEstado(request.getEstado() != null ? request.getEstado() : "borrador");
        oferta.setActivo(request.getActivo() != null ? request.getActivo() : true);
        
        // Guardar la oferta
        Oferta ofertaGuardada = ofertaRepository.save(oferta);
        
        // Agregar medias si existen
        if (request.getUrlsMedia() != null && !request.getUrlsMedia().isEmpty()) {
            List<OfertaMedia> medias = new ArrayList<>();
            for (String url : request.getUrlsMedia()) {
                OfertaMedia media = new OfertaMedia();
                media.setUrl(url);
                media.setOferta(ofertaGuardada);
                medias.add(ofertaMediaRepository.save(media));
            }
            ofertaGuardada.setMedias(medias);
        }
        
        return convertirAResponseDTO(ofertaGuardada);
    }
    
    @Transactional(readOnly = true)
    public List<OfertaResponseDTO> obtenerTodasLasOfertasActivas() {
        List<Oferta> ofertas = ofertaRepository.findByActivoTrue();
        return ofertas.stream()
            .map(this::convertirAResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<OfertaResponseDTO> obtenerOfertasPorCategoria(Long idCategoria) {
        // Validar que exista la categoría
        categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + idCategoria));
        
        List<Oferta> ofertas = ofertaRepository.findByCategoria(idCategoria);
        return ofertas.stream()
            .map(this::convertirAResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public OfertaResponseDTO actualizarOferta(Long id, OfertaRequestDTO request) {
        Oferta oferta = ofertaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + id));
        
        // Validar que exista la categoría si se proporciona
        if (request.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getIdCategoria()));
            oferta.setCategoria(categoria);
        }
        
        // Actualizar campos
        if (request.getProveedorId() != null) {
            oferta.setProveedorId(request.getProveedorId());
        }
        if (request.getTitulo() != null) {
            oferta.setTitulo(request.getTitulo());
        }
        if (request.getDescripcion() != null) {
            oferta.setDescripcion(request.getDescripcion());
        }
        if (request.getPrecioBase() != null) {
            oferta.setPrecioBase(request.getPrecioBase());
        }
        if (request.getEstado() != null) {
            oferta.setEstado(request.getEstado());
        }
        if (request.getActivo() != null) {
            oferta.setActivo(request.getActivo());
        }
        
        Oferta ofertaActualizada = ofertaRepository.save(oferta);
        return convertirAResponseDTO(ofertaActualizada);
    }
    
    @Transactional
    public void eliminarOferta(Long id) {
        Oferta oferta = ofertaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + id));
        
        // Borrado lógico
        oferta.setActivo(false);
        oferta.setEstado("archivado");
        ofertaRepository.save(oferta);
    }
    
    @Transactional
    public PrecioDescuentoDTO agregarDescuento(Long idOferta, PrecioDescuentoRequestDTO request) {
        Oferta oferta = ofertaRepository.findById(idOferta)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + idOferta));
        
        // Validar fechas
        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        PrecioDescuento descuento = new PrecioDescuento();
        descuento.setOferta(oferta);
        descuento.setNombre(request.getNombre());
        descuento.setTipoDescuento(request.getTipoDescuento());
        descuento.setFechaInicio(request.getFechaInicio());
        descuento.setFechaFin(request.getFechaFin());
        descuento.setValor(request.getValor());
        
        PrecioDescuento descuentoGuardado = precioDescuentoRepository.save(descuento);
        return convertirAPrecioDescuentoDTO(descuentoGuardado);
    }
    
    @Transactional
    public void eliminarDescuento(Long idOferta, Long idDescuento) {
        // Validar que exista la oferta
        ofertaRepository.findById(idOferta)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + idOferta));
        
        // Validar que exista el descuento
        PrecioDescuento descuento = precioDescuentoRepository.findById(idDescuento)
            .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado con ID: " + idDescuento));
        
        // Validar que el descuento pertenezca a la oferta
        if (!descuento.getOferta().getIdOfertas().equals(idOferta)) {
            throw new BadRequestException("El descuento no pertenece a la oferta especificada");
        }
        
        precioDescuentoRepository.delete(descuento);
    }
    
    // Métodos de conversión
    private OfertaResponseDTO convertirAResponseDTO(Oferta oferta) {
        OfertaResponseDTO dto = new OfertaResponseDTO();
        dto.setIdOfertas(oferta.getIdOfertas());
        dto.setProveedorId(oferta.getProveedorId());
        dto.setTitulo(oferta.getTitulo());
        
        // Categoría
        if (oferta.getCategoria() != null) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setIdCategoria(oferta.getCategoria().getIdCategoria());
            categoriaDTO.setDetalle(oferta.getCategoria().getDetalle());
            dto.setCategoria(categoriaDTO);
        }
        
        dto.setDescripcion(oferta.getDescripcion());
        dto.setPrecioBase(oferta.getPrecioBase());
        dto.setEstado(oferta.getEstado());
        dto.setCreadoEn(oferta.getCreadoEn());
        dto.setActualizadoEn(oferta.getActualizadoEn());
        dto.setActivo(oferta.getActivo());
        
        // Medias
        if (oferta.getMedias() != null) {
            dto.setMedias(oferta.getMedias().stream()
                .map(this::convertirAOfertaMediaDTO)
                .collect(Collectors.toList()));
        }
        
        // Descuentos
        if (oferta.getDescuentos() != null) {
            dto.setDescuentos(oferta.getDescuentos().stream()
                .map(this::convertirAPrecioDescuentoDTO)
                .collect(Collectors.toList()));
        }
        
        // Reseñas
        if (oferta.getResenas() != null) {
            dto.setResenas(oferta.getResenas().stream()
                .map(this::convertirAResenaDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private OfertaMediaDTO convertirAOfertaMediaDTO(OfertaMedia media) {
        OfertaMediaDTO dto = new OfertaMediaDTO();
        dto.setIdMedia(media.getIdMedia());
        dto.setUrl(media.getUrl());
        return dto;
    }
    
    private PrecioDescuentoDTO convertirAPrecioDescuentoDTO(PrecioDescuento descuento) {
        PrecioDescuentoDTO dto = new PrecioDescuentoDTO();
        dto.setIdDescuento(descuento.getIdDescuento());
        dto.setNombre(descuento.getNombre());
        dto.setTipoDescuento(descuento.getTipoDescuento());
        dto.setFechaInicio(descuento.getFechaInicio());
        dto.setFechaFin(descuento.getFechaFin());
        dto.setValor(descuento.getValor());
        return dto;
    }
    
    private ResenaDTO convertirAResenaDTO(Resena resena) {
        ResenaDTO dto = new ResenaDTO();
        dto.setIdResena(resena.getIdResena());
        dto.setUsuarioId(resena.getUsuarioId());
        dto.setCalificacion(resena.getCalificacion());
        dto.setComentario(resena.getComentario());
        dto.setFecha(resena.getFecha());
        return dto;
    }
}