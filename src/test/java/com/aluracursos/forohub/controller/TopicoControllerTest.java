package com.aluracursos.forohub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.aluracursos.forohub.dto.TopicoDetalleDto;
import com.aluracursos.forohub.dto.TopicoRequestDto;
import com.aluracursos.forohub.dto.TopicoResponseDto;
import com.aluracursos.forohub.services.TopicoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TopicoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TopicoService topicoService;

    @Test
    @DisplayName("POST /topicos - Debería registrar un nuevo tópico (201 Created)")
    @WithMockUser
    void testRegistrarTopico() throws Exception {
        // Arrange
        TopicoRequestDto topicoRequest = new TopicoRequestDto("Título", "Mensaje", 1L, 1L, null);
        TopicoResponseDto topicoResponse = new TopicoResponseDto(1L, "Título", "Mensaje", LocalDateTime.now(),
                "Pendiente", 1L, "usuario1", 1L, "curso1", 5);

        when(topicoService.registrarTopico(any(TopicoRequestDto.class))).thenReturn(topicoResponse);

        // Act & Assert
        mockMvc.perform(post("/topicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(topicoResponse.id()))
                .andExpect(jsonPath("$.titulo").value(topicoResponse.titulo()))
                .andExpect(jsonPath("$.mensaje").value(topicoResponse.mensaje()));
    }

    @Test
    @DisplayName("POST /topicos - Debería retornar error 400 Bad Request cuando se pasan datos inválidos")
    @WithMockUser
    void testRegistrarTopicoConDatosInvalidos() throws Exception {
        // Arrange
        TopicoRequestDto topicoRequest = new TopicoRequestDto("", "", null, null, null);

        // Act & Assert
        mockMvc.perform(post("/topicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /topicos - Debería retornar la lista de tópicos (200 OK)")
    void testListarTopicos() throws Exception {
        // Arrange
        TopicoResponseDto topico1 = new TopicoResponseDto(1L, "Título 1", "Mensaje 1", LocalDateTime.now(), "abierto",
                1L, "usuario1", 1L, "curso1", 0);
        TopicoResponseDto topico2 = new TopicoResponseDto(2L, "Título 2", "Mensaje 2", LocalDateTime.now(), "pendiente",
                2L, "usuario2", 2L, "curso2", 0);

        when(topicoService.listarTopicos()).thenReturn(List.of(topico1, topico2));

        // Act & Assert
        mockMvc.perform(get("/topicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Título 1"))
                .andExpect(jsonPath("$[1].titulo").value("Título 2"));

    }

    @Test
    @DisplayName("GET /topicos - Debería retornar la lista de tópicos paginados (200 OK)")
    @WithMockUser
    void testListarTopicosPaginados() throws Exception {
        // Arrange
        TopicoResponseDto topico1 = new TopicoResponseDto(1L, "Título 1", "Mensaje 1", LocalDateTime.now(), "abierto",
                1L,
                "usuario1", 1L, "curso1", 2);
        TopicoResponseDto topico2 = new TopicoResponseDto(2L, "Título 2", "Mensaje 2", LocalDateTime.now(), "pendiente",
                2L, "usuario2", 2L, "curso2", 0);

        Page<TopicoResponseDto> page = new PageImpl<>(List.of(topico1, topico2));

        when(topicoService.listarTopicosPaginados(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/topicos/paginados")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].titulo").value("Título 1"))
                .andExpect(jsonPath("$.content[1].titulo").value("Título 2"));

    }

    @Test
    @DisplayName("GET /topicos - Debería retornar la lista de tópicos filtrados (200 OK)")
    @WithMockUser
    void testListarTopicosFiltrados() throws Exception {
        // Arrange
        TopicoResponseDto topico1 = new TopicoResponseDto(1L, "Título 1", "Mensaje 1", LocalDateTime.now(), "abierto",
                1L, "usuario1", 1L, "curso1", 2);

        when(topicoService.listarTopicosFiltrados("curso1", 2025)).thenReturn(List.of(topico1));

        // Act & Assert
        mockMvc.perform(get("/topicos/filtrados")
                .param("nombreCurso", "curso1")
                .param("año", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Título 1"));

    }

    @Test
    @DisplayName("GET /topicos/{id} - Debería retornar un tópico por ID (200 OK)")
    @WithMockUser
    void testObtenerTopicoById() throws Exception {
        // Arrange
        Long topicoId = 1L;
        TopicoDetalleDto.AutorDto autorDto = new TopicoDetalleDto.AutorDto(1L, "usuario1");
        TopicoDetalleDto.CursoDto cursoDto = new TopicoDetalleDto.CursoDto(1L, "curso1");

        TopicoDetalleDto topicoDetalle = new TopicoDetalleDto(topicoId, "Título", "Mensaje", LocalDateTime.now(),
                "abierto", autorDto, cursoDto, List.of());

        when(topicoService.obtenerTopicoById(topicoId)).thenReturn(topicoDetalle);

        // Act & Assert
        mockMvc.perform(get("/topicos/{id}", topicoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(topicoId))
                .andExpect(jsonPath("$.titulo").value("Título"))
                .andExpect(jsonPath("$.mensaje").value("Mensaje"))
                .andExpect(jsonPath("$.autor.nombre").value("usuario1"))
                .andExpect(jsonPath("$.curso.nombre").value("curso1"));

    }

    @Test
    @DisplayName("PUT /topicos/{id} - Debería actualizar un tópico (200 OK)")
    @WithMockUser
    void testActualizarTopico() throws Exception {
        // Arrange
        Long topicoId = 1L;
        TopicoRequestDto topicoRequest = new TopicoRequestDto("Nuevo Título", "Nuevo Mensaje", topicoId, topicoId,
                null);
        TopicoResponseDto updatedTopico = new TopicoResponseDto(topicoId, "Nuevo Título", "Nuevo Mensaje",
                LocalDateTime.now(), "abierto", 1L, "usuario1", 1L, "curso1", 5);
        when(topicoService.actualizarTopico(topicoId, topicoRequest)).thenReturn(updatedTopico);

        // Act & Assert
        mockMvc.perform(put("/topicos/{id}", topicoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Nuevo Título"))
                .andExpect(jsonPath("$.mensaje").value("Nuevo Mensaje"));
    }

    @Test
    @DisplayName("DELETE /topicos/{id} - Debería eliminar un tópico (204 No Content)")
    @WithMockUser
    void testEliminarTopico() throws Exception {
        // Arrange
        Long topicoId = 1L;
        doNothing().when(topicoService).eliminarTopico(topicoId);

        // Act & Assert
        mockMvc.perform(delete("/topicos/1"))
                .andExpect(status().isNoContent());
    }

}
