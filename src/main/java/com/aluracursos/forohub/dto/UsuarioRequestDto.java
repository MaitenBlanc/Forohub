package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDto(

        @NotBlank(message = "El nombre no puede estar vacío") String nombre,
        @NotBlank(message = "El correo no puede estar vacío") @Email(message = "El correo debe ser válido") String correoElectronico,
        @NotBlank(message = "La contraseña no puede estar vacía") String contrasena,
        @NotNull(message = "El perfil no puede ser nulo") Long perfilId) {
}
