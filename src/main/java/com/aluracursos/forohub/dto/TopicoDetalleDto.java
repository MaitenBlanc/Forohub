package com.aluracursos.forohub.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TopicoDetalleDto", description = "Detalles de un tópico de discusión")
public record TopicoDetalleDto(
        @Schema(description = "ID del tópico", example = "1") Long id,
        @Schema(description = "Título del tópico", example = "¿Cómo implementar un patrón de diseño en Java?") String titulo,
        @Schema(description = "Mensaje del tópico", example = "Estoy intentando implementar el patrón Singleton en mi aplicación, pero no sé cómo hacerlo correctamente.") String mensaje,
        @Schema(description = "Fecha de creación del tópico", example = "2023-10-01T12:00:00") LocalDateTime fechaCreacion,
        @Schema(description = "Estado del tópico", example = "ABIERTO") String status,
        @Schema(description = "ID del autor del tópico", example = "123") AutorDto autor,
        @Schema(description = "ID del curso del tópico", example = "123") CursoDto curso,
        @Schema(description = "Lista de respuestas al tópico", example = "[{\"id\": 1, \"mensaje\": \"Respuesta 1\"}, {\"id\": 2, \"mensaje\": \"Respuesta 2\"}]") List<RespuestaDto> respuestas) {

    public record AutorDto(Long id, String nombre) {
    }

    public record CursoDto(Long id, String nombre) {
    }
}
