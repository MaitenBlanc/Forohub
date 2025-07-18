package com.aluracursos.forohub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aluracursos.forohub.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreoElectronico(String correoElectronico);

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

}
