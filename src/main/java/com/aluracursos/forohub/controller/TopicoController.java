package com.aluracursos.forohub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.aluracursos.forohub.dto.TopicoRequestDto;
import com.aluracursos.forohub.dto.TopicoResponseDto;
import com.aluracursos.forohub.services.TopicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Tópicos", description = "Gestión de tópicos de discusión")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    // Endpoint para registrar un nuevo tópico
    @Operation(summary = "Registrar nuevo tópico", description = "Crea un nuevo tópico de discusión en el foro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tópico creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoResponseDto> registrarTopico(
            @Parameter(description = "Datos para crear el tópico", required = true) @Valid @RequestBody TopicoRequestDto topicoRequestDto,
            UriComponentsBuilder uriBuilder) {
        var topicoResponse = topicoService.registrarTopico(topicoRequestDto);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoResponse.id()).toUri();

        return ResponseEntity.created(uri).body(topicoResponse);
    }

    // Endpoint para listar todos los tópicos
    @Operation(summary = "Listar todos los tópicos", description = "Obtiene una lista de todos los tópicos de discusión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tópicos obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @GetMapping
    public ResponseEntity<List<TopicoResponseDto>> listarTopicos() {
        var topicos = topicoService.listarTopicos();
        return ResponseEntity.ok(topicos);
    }

    // Endpoint para listar los tópicos con paginación y ordenamiento
    @Operation(summary = "Listar tópicos paginados", description = "Obtiene una lista de tópicos de discusión con paginación y ordenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tópicos paginados obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @GetMapping("/paginados")
    public ResponseEntity<Page<TopicoResponseDto>> listarTopicosPaginados(
            @Parameter(description = "Parámetros de paginación y ordenamiento", required = false) @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {
        var topicosPaginados = topicoService.listarTopicosPaginados(paginacion);
        return ResponseEntity.ok(topicosPaginados);
    }

    // Endpoint para listar los tópicos filtrados por curso y año
    @Operation(summary = "Listar tópicos filtrados", description = "Obtiene una lista de tópicos de discusión filtrados por curso y año")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tópicos filtrados obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtrado inválidos"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @GetMapping("/filtrados")
    public ResponseEntity<List<TopicoResponseDto>> listarTopicosFiltrados(
            @Parameter(description = "Nombre del curso para filtrar los tópicos", required = false) @RequestParam(required = false) String nombreCurso,
            @Parameter(description = "Año para filtrar los tópicos", required = false) @RequestParam(required = false) Integer año) {
        return ResponseEntity.ok(topicoService.listarTopicosFiltrados(nombreCurso, año));
    }

    // Endpoint para obtener detalles de un tópico específico
    @Operation(summary = "Obtener detalles de un tópico", description = "Obtiene los detalles de un tópico de discusión específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles del tópico obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tópico no encontrado"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TopicoDetalleDto> obtenerTopicoById(
            @Parameter(description = "ID del tópico a obtener", required = true) @PathVariable Long id) {
        TopicoDetalleDto topico = topicoService.obtenerTopicoById(id);
        return ResponseEntity.ok(topico);
    }

    // Endpoint para actualizar un tópico
    @Operation(summary = "Actualizar tópico", description = "Actualiza un tópico de discusión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tópico actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tópico no encontrado"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizarTopico(
            @Parameter(description = "ID del tópico a actualizar", required = true) @PathVariable Long id,
            @Valid @RequestBody TopicoRequestDto topicoRequestDto) {
        try {
            TopicoResponseDto topicoActualizado = topicoService.actualizarTopico(id, topicoRequestDto);
            return ResponseEntity.ok(topicoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // Endpoint para eliminar un tópico
    @Operation(summary = "Eliminar tópico", description = "Elimina un tópico de discusión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tópico eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tópico no encontrado"),
            @ApiResponse(responseCode = "401", description = "Autenticación requerida")
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(
            @Parameter(description = "ID del tópico a eliminar", required = true) @PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }
}
