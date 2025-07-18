package com.aluracursos.forohub.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.aluracursos.forohub.dto.*;
import com.aluracursos.forohub.entity.*;
import com.aluracursos.forohub.repository.*;

@ExtendWith(MockitoExtension.class)
public class TopicoServiceTest {
    @Mock
    private TopicoRepository topicoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private TopicoService topicoService;

    // Creacion de entidades
    private Perfil crearPerfil() {
        return new Perfil(1L, "usuario1", null);
    }

    private Categoria crearCategoria() {
        return new Categoria(1L, "programacion", LocalDateTime.now(), LocalDateTime.now(), null);
    }

    private Usuario crearUsuario() {
        return new Usuario(1L, "usuario1", "usuario1@example.com", "password", crearPerfil(), null, null);
    }

    private Curso crearCurso() {
        return new Curso(1L, "Curso de Programación", crearCategoria(), null);
    }

    private Topico crearTopico() {
        return new Topico(1L, "Titulo", "Mensaje", LocalDateTime.now(), "abierto", crearUsuario(),
                crearCurso(), List.of());
    }

    @Test
    @DisplayName("Debería registrar un nuevo tópico correctamente")
    void testRegistrarTopico() {
        // Arrange
        Usuario usuario = crearUsuario();
        Curso curso = crearCurso();
        Topico topico = crearTopico();

        TopicoRequestDto request = new TopicoRequestDto("Titulo", "Mensaje", 1L, 1L, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(topicoRepository.existsByTituloAndMensaje("Titulo", "Mensaje")).thenReturn(false);
        when(topicoRepository.save(any(Topico.class))).thenReturn(topico);

        // Act
        TopicoResponseDto response = topicoService.registrarTopico(request);

        // Assert
        assertNotNull(response);
        assertEquals("Titulo", response.titulo());
        assertEquals("usuario1", response.autorNombre());
        assertEquals("Curso de Programación", response.cursoNombre());
        verify(topicoRepository).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería retornar todos los tópicos")
    void testListarTopicos() {
        // Arrange
        Topico topico = crearTopico();
        when(topicoRepository.findAll()).thenReturn(List.of(topico));

        // Act
        List<TopicoResponseDto> response = topicoService.listarTopicos();

        // Assert
        assertEquals(1, response.size());
        TopicoResponseDto dto = response.get(0);
        assertEquals("Titulo", dto.titulo());
        assertEquals("usuario1", dto.autorNombre());
        assertEquals("Curso de Programación", dto.cursoNombre());

    }

    @Test
    @DisplayName("Debería retornar tópicos filtrados por curso y año")
    void testListarTopicosFiltrados() {
        // Arrange
        Curso curso = crearCurso();
        Topico topico = crearTopico();

        LocalDateTime inicio = Year.of(2025).atDay(1).atStartOfDay();
        LocalDateTime fin = Year.of(2025).atDay(Year.of(2025).length()).atTime(23, 59, 59);

        when(topicoRepository.findByCursoNombreAndFechaCreacionBetween(curso.getNombre(), inicio, fin))
                .thenReturn(List.of(topico));

        // Act
        List<TopicoResponseDto> response = topicoService.listarTopicosFiltrados(curso.getNombre(), 2025);

        // Assert
        assertEquals(1, response.size());
        TopicoResponseDto dto = response.get(0);
        assertEquals("Titulo", dto.titulo());
        assertEquals("Curso de Programación", dto.cursoNombre());
    }

    @Test
    @DisplayName("Debería retornar tópicos paginados")
    void testListarTopicosPaginados() {
        // Arrange
        Topico topico = crearTopico();
        Page<Topico> page = new PageImpl<>(List.of(topico));

        when(topicoRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<TopicoResponseDto> response = topicoService.listarTopicosPaginados(Pageable.unpaged());

        // Assert
        assertEquals(1, response.getTotalElements());
        assertEquals("Titulo", response.getContent().get(0).titulo());

    }

    @Test
    @DisplayName("Debería obtener un tópico por un ID")
    void testObtenerTopicoById() {
        // Arrange
        Topico topico = crearTopico();

        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));

        // Act
        TopicoDetalleDto response = topicoService.obtenerTopicoById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Titulo", response.titulo());
        assertEquals("usuario1", response.autor().nombre());
        assertEquals("Curso de Programación", response.curso().nombre());
        assertTrue(response.respuestas().isEmpty());
    }

    @Test
    @DisplayName("Debería actualizar un tópico existente")
    void testActualizarTopico() {
        // Arrange
        Usuario usuario = crearUsuario();
        Curso curso = crearCurso();
        Topico topicoExistente = crearTopico();
        Topico topicoActualizado = new Topico(1L, "Nuevo Título", "Nuevo Mensaje", topicoExistente.getFechaCreacion(),
                "abierto", usuario, curso, List.of());

        TopicoRequestDto request = new TopicoRequestDto("Nuevo Título", "Nuevo Mensaje", 1L, 1L, null);
        
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topicoExistente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(topicoRepository.existsByTituloAndMensajeAndIdNot(eq("Nuevo Título"), eq("Nuevo Mensaje"), anyLong()))
        .thenReturn(false);
        when(topicoRepository.save(any(Topico.class))).thenReturn(topicoActualizado);
        
        // Act
        TopicoResponseDto response = topicoService.actualizarTopico(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Nuevo Título", response.titulo());
        assertEquals("Nuevo Mensaje", response.mensaje());
        assertEquals("usuario1", response.autorNombre());
        assertEquals("Curso de Programación", response.cursoNombre());

        verify(topicoRepository).findById(1L);
        verify(topicoRepository).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería eliminar un tópico existente")
    void testEliminarTopico() {
        // Arrange
        when(topicoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(topicoRepository).deleteById(1L);

        // Act
        topicoService.eliminarTopico(1L);

        // Assert
        verify(topicoRepository, times(1)).deleteById(1L);
    }

    // Test que devuelven errores
    @Test
    @DisplayName("Debería lanzar excepción al intentar registrar un tópico con título y mensaje duplicados")
    void testRegistrarTopicoDuplicado() {
        // Arrange
        TopicoRequestDto request = new TopicoRequestDto("Titulo", "Mensaje", 1L, 1L, null);

        when(topicoRepository.existsByTituloAndMensaje("Titulo", "Mensaje")).thenReturn(true);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            topicoService.registrarTopico(request);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción al intentar obtener un tópico inexistente")
    void testObtenerTopicoByIdInexistente() {
        // Arrange
        when(topicoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            topicoService.obtenerTopicoById(99L);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción al intentar actualizar un tópico inexistente")
    void testActualizarTopicoInexistente() {
        // Arrange
        TopicoRequestDto request = new TopicoRequestDto("Nuevo Título", "Nuevo Mensaje", 1L, 1L, null);

        when(topicoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            topicoService.actualizarTopico(99L, request);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción al intentar eliminar un tópico inexistente")
    void testEliminarTopicoInexistente() {
        // Arrange
        when(topicoRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            topicoService.eliminarTopico(99L);
        });
    }
}
