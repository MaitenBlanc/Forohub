package com.aluracursos.forohub.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TopicoResponseDto", description = "Datos requeridos para crear un tópico")
public record TopicoResponseDto(
        @Schema(description = "ID del tópico", example = "1") Long id,
        @Schema(description = "Título del tópico", example = "¿Cómo implementar un patrón de diseño en Java?") String titulo,
        @Schema(description = "Mensaje del tópico", example = "Estoy intentando implementar el patrón Singleton en mi aplicación, pero no sé cómo hacerlo correctamente.") String mensaje,
        @Schema(description = "Fecha de creación del tópico", example = "2023-10-01T12:00:00") LocalDateTime fechaCreacion,
        @Schema(description = "Estado del tópico", example = "ABIERTO") String status,
        @Schema(description = "ID del autor del tópico", example = "123") Long autorId,
        @Schema(description = "Nombre del autor del tópico", example = "Juan Pérez") String autorNombre,
        @Schema(description = "ID del curso al que pertenece el tópico", example = "101") Long cursoId,
        @Schema(description = "Nombre del curso al que pertenece el tópico", example = "Curso de Java Avanzado") String cursoNombre,
        @Schema(description = "Número total de respuestas al tópico", example = "5") Integer totalRespuestas) {

}
