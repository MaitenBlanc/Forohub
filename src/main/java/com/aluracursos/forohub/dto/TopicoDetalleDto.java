package com.aluracursos.forohub.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TopicoDetalleDto(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String status,
        AutorDto autor,
        CursoDto curso,
        List<RespuestaDto> respuestas) {

    public record AutorDto(Long id, String nombre) {
    }

    public record CursoDto(Long id, String nombre) {
    }
}
