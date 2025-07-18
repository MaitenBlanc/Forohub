package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.dto.CursoRequestDto;
import com.aluracursos.forohub.dto.CursoResponseDto;
import com.aluracursos.forohub.services.CursoService;

import jakarta.validation.Valid;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoResponseDto> registrarCurso(@RequestBody @Valid CursoRequestDto cursoRequestDto,
            UriComponentsBuilder uriBuilder) {
        CursoResponseDto cursoResponse = cursoService.registrarCurso(cursoRequestDto);
        URI uri = uriBuilder.path("/cursos/{id}")
                .buildAndExpand(cursoResponse.id()).toUri();

        return ResponseEntity.created(uri).body(cursoResponse);
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> obtenerCursoPorId(@PathVariable Long id) {
        var cursoResponse = cursoService.obtenerCursoPorId(id);
        return ResponseEntity.ok(cursoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDto> actualizarCurso(@PathVariable Long id,
            @RequestBody @Valid CursoRequestDto cursoRequestDto) {
        var cursoResponse = cursoService.actualizarCurso(id, cursoRequestDto);

        return ResponseEntity.ok(cursoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}
