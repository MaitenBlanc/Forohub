package com.aluracursos.forohub.services;

import com.aluracursos.forohub.dto.CursoRequestDto;
import com.aluracursos.forohub.dto.CursoResponseDto;
import com.aluracursos.forohub.entity.Curso;
import com.aluracursos.forohub.entity.Categoria;
import com.aluracursos.forohub.repository.CursoRepository;
import com.aluracursos.forohub.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class CursoService {

    private CursoRepository cursoRepository;
    private CategoriaRepository categoriaRepository;

    public CursoService(CursoRepository cursoRepository, CategoriaRepository categoriaRepository) {
        this.cursoRepository = cursoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CursoResponseDto registrarCurso(CursoRequestDto cursoRequestDto) {
        if (cursoRepository.existsByNombre(cursoRequestDto.nombre())) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un curso con este nombre");
        }

        Categoria categoria = categoriaRepository.findById(cursoRequestDto.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "No se encontró la categoría con ID: " + cursoRequestDto.categoriaId()));

        Curso curso = new Curso();
        curso.setNombre(cursoRequestDto.nombre());
        curso.setCategoria(categoria);

        Curso cursoGuardado = cursoRepository.save(curso);

        return new CursoResponseDto(cursoGuardado.getId(), cursoGuardado.getNombre(),
                cursoGuardado.getCategoria().getId());
    }

    public List<CursoResponseDto> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(curso -> new CursoResponseDto(
                        curso.getId(),
                        curso.getNombre(),
                        curso.getCategoria().getId()))
                .toList();
    }

    public CursoResponseDto obtenerCursoPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Curso no encontrado con ID: " + id));

        return new CursoResponseDto(curso.getId(), curso.getNombre(), curso.getCategoria().getId());
    }

    @Transactional
    public CursoResponseDto actualizarCurso(Long id, CursoRequestDto cursoRequestDto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Curso no encontrado con ID: " + id));

        if (cursoRepository.existsByNombre(cursoRequestDto.nombre()) &&
                !curso.getNombre().equals(cursoRequestDto.nombre())) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un curso con este nombre");
        }

        Categoria categoria = categoriaRepository.findById(cursoRequestDto.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "No se encontró la categoría con ID: " + cursoRequestDto.categoriaId()));

        curso.setNombre(cursoRequestDto.nombre());
        curso.setCategoria(categoria);

        Curso cursoActualizado = cursoRepository.save(curso);

        return new CursoResponseDto(cursoActualizado.getId(), cursoActualizado.getNombre(),
                cursoActualizado.getCategoria().getId());
    }

    @Transactional
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}
