package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.dto.CategoriaRequestDto;
import com.aluracursos.forohub.dto.CategoriaResponseDto;
import com.aluracursos.forohub.services.CategoriaService;
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
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> registrarCategoria(
            @RequestBody @Valid CategoriaRequestDto categoriaRequestDto,
            UriComponentsBuilder uriBuilder) {

        CategoriaResponseDto categoriaResponse = categoriaService.registrarCategoria(categoriaRequestDto);
        URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(categoriaResponse.id()).toUri();

        return ResponseEntity.created(uri).body(categoriaResponse);
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDto>> listarCategorias(
            @PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {

        return ResponseEntity.ok(categoriaService.listarCategorias(paginacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> obtenerCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody @Valid CategoriaRequestDto categoriaRequestDto) {

        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoriaRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

}
