package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RespuestaRequestDto(
        @NotBlank(message = "El mensaje no puede estar vacío") String mensaje,
        @NotNull(message = "El ID del tópico no puede ser nulo") Long topicoId,
        @NotNull(message = "El ID del autor no puede ser nulo") Long autorId,
        Boolean solucion) {
}
