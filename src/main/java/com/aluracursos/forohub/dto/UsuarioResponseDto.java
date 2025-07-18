package com.aluracursos.forohub.dto;

public record UsuarioResponseDto(
        Long id,
        String nombre,
        String correoElectronico,
        Long perfilId) {

}
