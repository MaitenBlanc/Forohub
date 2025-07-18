package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoRequestDto(
                @NotBlank(message = "El título no puede estar vacío") String titulo,
                @NotBlank(message = "El mensaje no puede estar vacío") String mensaje,
                @NotNull(message = "El autor no puede ser nulo") Long autorId,
                @NotNull(message = "El curso no puede ser nulo") Long cursoId,
                Boolean solucion) {

}
