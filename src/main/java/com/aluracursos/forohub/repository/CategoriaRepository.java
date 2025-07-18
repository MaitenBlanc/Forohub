package com.aluracursos.forohub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aluracursos.forohub.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
