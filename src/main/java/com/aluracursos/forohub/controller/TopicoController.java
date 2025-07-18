package com.aluracursos.forohub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.aluracursos.forohub.dto.TopicoRequestDto;
import com.aluracursos.forohub.dto.TopicoResponseDto;
import com.aluracursos.forohub.services.TopicoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.aluracursos.forohub.dto.TopicoDetalleDto;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {
    @Autowired
    private TopicoService topicoService;

    // Endpoint para registrar un nuevo tópico
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoResponseDto> registrarTopico(@Valid @RequestBody TopicoRequestDto topicoRequestDto,
            UriComponentsBuilder uriBuilder) {
        var topicoResponse = topicoService.registrarTopico(topicoRequestDto);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoResponse.id()).toUri();

        return ResponseEntity.created(uri).body(topicoResponse);
    }

    // Endpoint para listar todos los tópicos
    @GetMapping
    public ResponseEntity<List<TopicoResponseDto>> listarTopicos() {
        var topicos = topicoService.listarTopicos();
        return ResponseEntity.ok(topicos);
    }

    // Endpoint para listar los tópicos con paginación y ordenamiento
    @GetMapping("/paginados")
    public ResponseEntity<Page<TopicoResponseDto>> listarTopicosPaginados(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {
        var topicosPaginados = topicoService.listarTopicosPaginados(paginacion);
        return ResponseEntity.ok(topicosPaginados);
    }

    // Endpoint para listar los tópicos filtrados por curso y año
    @GetMapping("/filtrados")
    public ResponseEntity<List<TopicoResponseDto>> listarTopicosFiltrados(
            @RequestParam(required = false) String nombreCurso,
            @RequestParam(required = false) Integer año) {
        return ResponseEntity.ok(topicoService.listarTopicosFiltrados(nombreCurso, año));
    }

    // Endpoint para obtener detalles de un tópico específico
    @GetMapping("/{id}")
    public ResponseEntity<TopicoDetalleDto> obtenerTopicoById(@PathVariable Long id) {
        TopicoDetalleDto topico = topicoService.obtenerTopicoById(id);
        return ResponseEntity.ok(topico);
    }

    // Endpoint para actualizar un tópico
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizarTopico(@PathVariable Long id,
            @Valid @RequestBody TopicoRequestDto topicoRequestDto) {
        try {
            TopicoResponseDto topicoActualizado = topicoService.actualizarTopico(id, topicoRequestDto);
            return ResponseEntity.ok(topicoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // Endpoint para eliminar un tópico
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }
}
