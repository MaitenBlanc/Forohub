package com.aluracursos.forohub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aluracursos.forohub.entity.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNombre(String nombre);

    
}
