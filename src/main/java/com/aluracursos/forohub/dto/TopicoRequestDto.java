package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TopicoRequestDto", description = "Datos requeridos para crear un tópico")
public record TopicoRequestDto(
        @Schema(description = "Título del tópico", example = "¿Cómo implementar un patrón de diseño en Java?", required = true) @NotBlank(message = "El título no puede estar vacío") String titulo,
        @Schema(description = "Mensaje del tópico", example = "Estoy intentando implementar el patrón Singleton en mi aplicación, pero no sé cómo hacerlo correctamente.", required = true) @NotBlank(message = "El mensaje no puede estar vacío") String mensaje,
        @Schema(description = "ID del autor del tópico", example = "123", required = true) @NotNull(message = "El autor no puede ser nulo") Long autorId,
        @Schema(description = "ID del curso al que pertenece el tópico", example = "101", required = true) @NotNull(message = "El curso no puede ser nulo") Long cursoId,
        @Schema(description = "Indica si el tópico es una solución", example = "false") Boolean solucion) {

}
