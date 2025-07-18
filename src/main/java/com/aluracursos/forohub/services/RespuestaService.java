package com.aluracursos.forohub.services;

import com.aluracursos.forohub.dto.RespuestaRequestDto;
import com.aluracursos.forohub.dto.RespuestaResponseDto;
import com.aluracursos.forohub.entity.*;
import com.aluracursos.forohub.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Service
public class RespuestaService {

        private RespuestaRepository respuestaRepository;
        private TopicoRepository topicoRepository;
        private UsuarioRepository usuarioRepository;

        public RespuestaService(RespuestaRepository respuestaRepository,
                        TopicoRepository topicoRepository,
                        UsuarioRepository usuarioRepository) {
                this.respuestaRepository = respuestaRepository;
                this.topicoRepository = topicoRepository;
                this.usuarioRepository = usuarioRepository;
        }

        @Transactional
        public RespuestaResponseDto registrarRespuesta(RespuestaRequestDto respuestaRequestDto) {
                // Verificar si el tópico existe
                Topico topico = topicoRepository.findById(respuestaRequestDto.topicoId())
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el tópico con ID: " + respuestaRequestDto.topicoId()));

                // Verificar si el autor existe
                Usuario autor = usuarioRepository.findById(respuestaRequestDto.autorId())
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                                "No se encontró el autor con ID: " + respuestaRequestDto.autorId()));

                // Crear y guardar la nueva respuesta
                Respuesta respuesta = Respuesta.builder()
                                .mensaje(respuestaRequestDto.mensaje())
                                .topico(topico)
                                .autor(autor)
                                .fechaCreacion(LocalDateTime.now())
                                .solucion(respuestaRequestDto.solucion() != null ? respuestaRequestDto.solucion()
                                                : false)
                                .build();

                Respuesta savedRespuesta = respuestaRepository.save(respuesta);

                // Agregar la respuesta al tópico
                topico.getRespuestas().add(savedRespuesta);
                topicoRepository.save(topico);

                // Devolver la respuesta creada
                return new RespuestaResponseDto(savedRespuesta.getId(), savedRespuesta.getMensaje(),
                                savedRespuesta.getTopico().getId(), savedRespuesta.getFechaCreacion(),
                                savedRespuesta.getAutor().getId(), savedRespuesta.getSolucion());
        }
}
