package com.aluracursos.forohub.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.aluracursos.forohub.dto.TopicoRequestDto;
import com.aluracursos.forohub.entity.Curso;
import com.aluracursos.forohub.entity.Topico;
import com.aluracursos.forohub.entity.Usuario;
import com.aluracursos.forohub.repository.CursoRepository;
import com.aluracursos.forohub.repository.TopicoRepository;
import com.aluracursos.forohub.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class TopicoServiceTest {
    @Mock
    private TopicoRepository topicoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private TopicoService topicoService;

    @Test
    @DisplayName("Debería registrar un nuevo tópico correctamente")
    void testRegistrarTopico() {
        // Arrange
        var request = new TopicoRequestDto("Titulo", "Mensaje", 1L, 1L, null);
        var usuario = new Usuario();
        usuario.setId(1L);
        var curso = new Curso();
        curso.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(topicoRepository.existsByTituloAndMensaje("Titulo", "Mensaje")).thenReturn(false);
        when(topicoRepository.save(any(Topico.class))).thenAnswer(inv -> {
            Topico topico = inv.getArgument(0);
            topico.setId(1L);
            return topico;
        });

        // Act
        var response = topicoService.registrarTopico(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Titulo", response.titulo());
        verify(topicoRepository, times(1)).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar registrar un tópico existente")
    void registrarTopicoExistenteTest() {
        // Arrange
        var request = new TopicoRequestDto("Titulo", "Mensaje", 1L, 1L, null);
        when(topicoRepository.existsByTituloAndMensaje("Titulo", "Mensaje")).thenReturn(true);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> topicoService.registrarTopico(request));
    }

    @Test
    void testActualizarTopico() {

    }

    @Test
    void testEliminarTopico() {

    }

    @Test
    void testListarTopicos() {

    }

    @Test
    void testListarTopicosFiltrados() {

    }

    @Test
    void testListarTopicosPaginados() {

    }

    @Test
    void testObtenerTopicoById() {

    }

}
