package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoRequestDto(
                @NotBlank(message = "El nombre del curso no puede estar vacío") String nombre,
                @NotNull(message = "el id de la categoría del curso no puede ser nulo") Long categoriaId) {

}
