package com.aluracursos.forohub.dto;

import java.time.LocalDateTime;

public record CategoriaResponseDto(
        Long id,
        String nombre,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {
}
