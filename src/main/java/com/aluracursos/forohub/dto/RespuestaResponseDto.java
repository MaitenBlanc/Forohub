package com.aluracursos.forohub.dto;

import java.time.LocalDateTime;

public record RespuestaResponseDto(
        Long id,
        String mensaje,
        Long topicoId,
        LocalDateTime fechaCreacion,
        Long autorId,
        Boolean solucion) {

}
