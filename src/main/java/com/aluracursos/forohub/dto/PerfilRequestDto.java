package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotBlank;

public record PerfilRequestDto(
        @NotBlank(message = "El nombre del perfil es obligatorio") String nombre) {

}
