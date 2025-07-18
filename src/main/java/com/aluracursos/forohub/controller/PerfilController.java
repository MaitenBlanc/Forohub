package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.dto.PerfilRequestDto;
import com.aluracursos.forohub.dto.PerfilResponseDto;
import com.aluracursos.forohub.services.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @PostMapping
    public ResponseEntity<PerfilResponseDto> registrarPerfil(
            @RequestBody @Valid PerfilRequestDto perfilRequestDto,
            UriComponentsBuilder uriBuilder) {

        PerfilResponseDto perfilResponse = perfilService.registrarPerfil(perfilRequestDto);
        URI uri = uriBuilder.path("/perfiles/{id}").buildAndExpand(perfilResponse.id()).toUri();

        return ResponseEntity.created(uri).body(perfilResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PerfilResponseDto>> listarPerfiles(
            @PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {

        return ResponseEntity.ok(perfilService.listarPerfiles(paginacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDto> obtenerPerfilPorId(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.obtenerPerfilPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDto> actualizarPerfil(
            @PathVariable Long id,
            @RequestBody @Valid PerfilRequestDto perfilRequestDto) {

        return ResponseEntity.ok(perfilService.actualizarPerfil(id, perfilRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable Long id) {
        perfilService.eliminarPerfil(id);
        return ResponseEntity.noContent().build();
    }
}