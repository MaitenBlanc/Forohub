package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDto(
        @NotBlank(message = "El nombre de la categoría es obligatorio") String nombre) {

}
