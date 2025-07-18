package com.aluracursos.forohub.controller;

import java.net.URI;
import com.aluracursos.forohub.dto.RespuestaRequestDto;
import com.aluracursos.forohub.dto.RespuestaResponseDto;
import com.aluracursos.forohub.services.RespuestaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {
    private RespuestaService respuestaService;

    public RespuestaController(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    @PostMapping
    public ResponseEntity<RespuestaResponseDto> registrarRespuesta(
            @Valid @RequestBody RespuestaRequestDto respuestaRequestDto,
            UriComponentsBuilder uriBuilder) {
        RespuestaResponseDto respuestaResponseDto = respuestaService.registrarRespuesta(respuestaRequestDto);
        URI uri = uriBuilder.path("/respuestas/{id}")
                .buildAndExpand(respuestaResponseDto.id()).toUri();

        return ResponseEntity.created(uri).body(respuestaResponseDto);
    }
}
