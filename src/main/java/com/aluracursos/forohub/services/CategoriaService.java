package com.aluracursos.forohub.services;

import com.aluracursos.forohub.dto.CategoriaRequestDto;
import com.aluracursos.forohub.dto.CategoriaResponseDto;
import com.aluracursos.forohub.entity.Categoria;
import com.aluracursos.forohub.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponseDto registrarCategoria(CategoriaRequestDto categoriaRequestDto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRequestDto.nombre());

        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        return new CategoriaResponseDto(
                categoriaGuardada.getId(),
                categoriaGuardada.getNombre(),
                categoriaGuardada.getFechaCreacion(),
                LocalDateTime.now());
    }

    @Transactional
    public Page<CategoriaResponseDto> listarCategorias(Pageable paginacion) {
        return categoriaRepository.findAll(paginacion)
                .map(categoria -> new CategoriaResponseDto(
                        categoria.getId(),
                        categoria.getNombre(),
                        categoria.getFechaCreacion(),
                        categoria.getFechaActualizacion()));
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDto obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        return new CategoriaResponseDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getFechaCreacion(),
                categoria.getFechaActualizacion());
    }

    @Transactional
    public CategoriaResponseDto actualizarCategoria(Long id, CategoriaRequestDto categoriaRequestDto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoria.setNombre(categoriaRequestDto.nombre());

        Categoria categoriaActualizada = categoriaRepository.save(categoria);

        return new CategoriaResponseDto(
                categoriaActualizada.getId(),
                categoriaActualizada.getNombre(),
                categoriaActualizada.getFechaCreacion(),
                categoriaActualizada.getFechaActualizacion());
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}