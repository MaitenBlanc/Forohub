package com.aluracursos.forohub.dto;

import java.time.LocalDateTime;

public record RespuestaDto(
                Long id,
                String mensaje,
                LocalDateTime fechaCreacion,
                AutorDto autor,
                Boolean solucion) {

        public record AutorDto(Long id, String nombre) {
        }
}
