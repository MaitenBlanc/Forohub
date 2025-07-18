package com.aluracursos.forohub.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.aluracursos.forohub.dto.UsuarioRequestDto;
import com.aluracursos.forohub.dto.UsuarioResponseDto;
import com.aluracursos.forohub.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto,
            UriComponentsBuilder uriBuilder) {

        UsuarioResponseDto usuarioResponse = usuarioService.registrarUsuario(usuarioRequestDto);
        URI uri = uriBuilder.path("/usuarios/{id}")
                .buildAndExpand(usuarioResponse.id()).toUri();

        return ResponseEntity.created(uri).body(usuarioResponse);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {

        UsuarioResponseDto usuarioResponse = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(@PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {

        UsuarioResponseDto usuarioResponse = usuarioService.actualizarUsuario(id, usuarioRequestDto);
        return ResponseEntity.ok(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
