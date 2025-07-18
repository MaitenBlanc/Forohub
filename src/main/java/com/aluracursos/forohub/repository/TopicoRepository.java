package com.aluracursos.forohub.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aluracursos.forohub.entity.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    // Método para buscar tópicos por nombre del curso
    public List<Topico> findByCursoNombre(String nombreCurso);

    // Método para buscar tópicos por rango de fechas
    public List<Topico> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Método para buscar tópicos por nombre del curso y rango de fechas
    public List<Topico> findByCursoNombreAndFechaCreacionBetween(String nombreCurso, LocalDateTime fechaInicio,
            LocalDateTime fechaFin);

    // Consulta personalizada para ordenar por fecha ascendente
    @Query("SELECT t FROM Topico t ORDER BY t.fechaCreacion ASC")
    List<Topico> findAllOrderByFechaCreacionAsc();

    public boolean existsByTituloAndMensajeAndIdNot(String titulo, String mensaje, Long id);
}
