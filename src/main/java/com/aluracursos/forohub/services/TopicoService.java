package com.aluracursos.forohub.services;

import java.time.LocalDateTime;

import com.aluracursos.forohub.dto.TopicoRequestDto;
import com.aluracursos.forohub.dto.TopicoResponseDto;
import com.aluracursos.forohub.entity.*;
import com.aluracursos.forohub.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.springframework.http.HttpStatus.*;

import com.aluracursos.forohub.dto.RespuestaDto;
import com.aluracursos.forohub.dto.TopicoDetalleDto;

@Service
public class TopicoService {
        private TopicoRepository topicoRepository;
        private UsuarioRepository usuarioRepository;
        private CursoRepository cursoRepository;

        public TopicoService(TopicoRepository topicoRepository,
                        UsuarioRepository usuarioRepository,
                        CursoRepository cursoRepository) {
                this.topicoRepository = topicoRepository;
                this.usuarioRepository = usuarioRepository;
                this.cursoRepository = cursoRepository;
        }

        @Transactional
        public TopicoResponseDto registrarTopico(TopicoRequestDto topicoRequestDto) {
                // Verificar si existe un tópico con el mismo título y mensaje
                if (topicoRepository.existsByTituloAndMensaje(
                                topicoRequestDto.titulo(), topicoRequestDto.mensaje())) {
                        throw new ResponseStatusException(BAD_REQUEST,
                                        "Ya existe un tópico con el mismo título y mensaje");
                }

                // Obtener el autor y curso
                Usuario autor = usuarioRepository.findById(topicoRequestDto.autorId())
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el autor con ID: " + topicoRequestDto.autorId()));

                Curso curso = cursoRepository.findById(topicoRequestDto.cursoId())
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el curso con ID: " + topicoRequestDto.cursoId()));

                // Crear y guardar el nuevo tópico
                Topico topico = Topico.builder()
                                .titulo(topicoRequestDto.titulo())
                                .mensaje(topicoRequestDto.mensaje())
                                .autor(autor)
                                .curso(curso)
                                .fechaCreacion(LocalDateTime.now())
                                .status("abierto")
                                .build();

                Topico savedTopico = topicoRepository.save(topico);

                // Devolver el tópico creado
                return new TopicoResponseDto(savedTopico.getId(), savedTopico.getTitulo(), savedTopico.getMensaje(),
                                savedTopico.getFechaCreacion(), savedTopico.getStatus(), savedTopico.getAutor().getId(),
                                savedTopico.getAutor().getNombre(), savedTopico.getCurso().getId(),
                                savedTopico.getCurso().getNombre(), savedTopico.getTotalRespuestas());
        }

        // Método para listar todos los tópicos
        public List<TopicoResponseDto> listarTopicos() {
                return topicoRepository.findAll().stream()
                                .map(this::convertirAResponseDto)
                                .collect(Collectors.toList());
        }

        // Método para listar los tópicos con paginación y ordenamiento
        public Page<TopicoResponseDto> listarTopicosPaginados(Pageable paginacion) {
                return topicoRepository.findAll(paginacion).map(this::convertirAResponseDto);
        }

        // Método para listar los tópicos filtrados por curso y año
        public List<TopicoResponseDto> listarTopicosFiltrados(String nombreCurso, Integer año) {
                List<Topico> topicos;

                if (nombreCurso != null && año != null) {
                        LocalDateTime fechaInicio = Year.of(año).atDay(1).atStartOfDay();
                        LocalDateTime fechaFin = Year.of(año).atDay(Year.of(año).length()).atTime(23, 59, 59);

                        topicos = topicoRepository.findByCursoNombreAndFechaCreacionBetween(nombreCurso, fechaInicio,
                                        fechaFin);
                } else if (nombreCurso != null) {
                        topicos = topicoRepository.findByCursoNombre(nombreCurso);
                } else if (año != null) {
                        LocalDateTime fechaInicio = Year.of(año).atDay(1).atStartOfDay();
                        LocalDateTime fechaFin = Year.of(año).atDay(Year.of(año).length()).atTime(23, 59, 59);
                        topicos = topicoRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
                } else {
                        topicos = topicoRepository.findAll();
                }

                return topicos.stream()
                                .map(this::convertirAResponseDto)
                                .collect(Collectors.toList());
        }

        // Método para obtener detalles de un tópico específico
        public TopicoDetalleDto obtenerTopicoById(Long id) {
                Topico topico = topicoRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el tópico con ID: " + id));

                return convertirADetalleDto(topico);
        }

        // Método para actualizar un tópico
        @Transactional
        public TopicoResponseDto actualizarTopico(Long id, TopicoRequestDto topicoRequestDto) {
                Topico topico = topicoRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el tópico con ID: " + id));

                // Verificar si existe un tópico con el mismo título y mensaje
                if (topicoRepository.existsByTituloAndMensajeAndIdNot(
                                topicoRequestDto.titulo(), topicoRequestDto.mensaje(), id)) {
                        throw new ResponseStatusException(BAD_REQUEST,
                                        "Ya existe un tópico con el mismo título y mensaje");
                }

                // Obtener y validar autor y curso
                Usuario autor = usuarioRepository.findById(topicoRequestDto.autorId())
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el autor con ID: " + topicoRequestDto.autorId()));

                Curso curso = cursoRepository.findById(topicoRequestDto.cursoId())
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el curso con ID: " + topicoRequestDto.cursoId()));

                // Actualizar los campos del tópico
                topico.setTitulo(topicoRequestDto.titulo());
                topico.setMensaje(topicoRequestDto.mensaje());
                topico.setAutor(autor);
                topico.setCurso(curso);

                // Guardar los cambios
                Topico topicoActualizado = topicoRepository.save(topico);

                return convertirAResponseDto(topicoActualizado);
        }

        // Método para eliminar un tópico
        @Transactional
        public void eliminarTopico(Long id) {
                if (!topicoRepository.existsById(id)) {
                        throw new ResponseStatusException(NOT_FOUND,
                                        "No se encontró el tópico con ID: " + id);
                }
                topicoRepository.deleteById(id);
        }

        // Métodos privados para convertir entidades a DTOs
        private TopicoResponseDto convertirAResponseDto(Topico topico) {
                return new TopicoResponseDto(
                                topico.getId(),
                                topico.getTitulo(),
                                topico.getMensaje(),
                                topico.getFechaCreacion(),
                                topico.getStatus(),
                                topico.getAutor() != null ? topico.getAutor().getId() : null,
                                topico.getAutor() != null ? topico.getAutor().getNombre() : null, // autorNombre
                                topico.getCurso() != null ? topico.getCurso().getId() : null,
                                topico.getCurso() != null ? topico.getCurso().getNombre() : null, // cursoNombre
                                topico.getTotalRespuestas());
        }

        private TopicoDetalleDto convertirADetalleDto(Topico topico) {
                return new TopicoDetalleDto(
                                topico.getId(),
                                topico.getTitulo(),
                                topico.getMensaje(),
                                topico.getFechaCreacion(),
                                topico.getStatus(),
                                new TopicoDetalleDto.AutorDto(
                                                topico.getAutor().getId(),
                                                topico.getAutor().getNombre()),
                                new TopicoDetalleDto.CursoDto(
                                                topico.getCurso().getId(),
                                                topico.getCurso().getNombre()),
                                topico.getRespuestas().stream()
                                                .map(this::convertirARespuestaDto)
                                                .toList());
        }

        private RespuestaDto convertirARespuestaDto(Respuesta respuesta) {
                return new RespuestaDto(
                                respuesta.getId(),
                                respuesta.getMensaje(),
                                respuesta.getFechaCreacion(),
                                new RespuestaDto.AutorDto(
                                                respuesta.getAutor().getId(),
                                                respuesta.getAutor().getNombre()),
                                respuesta.getSolucion());
        }
}
