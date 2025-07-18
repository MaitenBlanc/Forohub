package com.aluracursos.forohub.services;

import com.aluracursos.forohub.dto.UsuarioRequestDto;
import com.aluracursos.forohub.dto.UsuarioResponseDto;
import com.aluracursos.forohub.entity.Usuario;
import com.aluracursos.forohub.entity.Perfil;
import com.aluracursos.forohub.repository.UsuarioRepository;
import com.aluracursos.forohub.repository.PerfilRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PerfilRepository perfilRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDto registrarUsuario(UsuarioRequestDto usuarioRequestDto) {
        if (usuarioRepository.existsByCorreoElectronico(usuarioRequestDto.correoElectronico())) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un usuario con este correo electrónico");
        }

        Perfil perfil = perfilRepository.findById(usuarioRequestDto.perfilId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "No se encontró el perfil con ID: " + usuarioRequestDto.perfilId()));

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRequestDto.nombre());
        usuario.setCorreoElectronico(usuarioRequestDto.correoElectronico());
        usuario.setContrasena(passwordEncoder.encode(usuarioRequestDto.contrasena()));
        usuario.setPerfil(perfil);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new UsuarioResponseDto(usuarioGuardado.getId(),
                usuarioGuardado.getNombre(), usuarioGuardado.getCorreoElectronico(),
                usuarioGuardado.getPerfil().getId());
    }

    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioResponseDto(usuario.getId(), usuario.getNombre(),
                        usuario.getCorreoElectronico(), usuario.getPerfil().getId()))
                .toList();
    }

    public UsuarioResponseDto obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado con ID: " + id));

        return new UsuarioResponseDto(usuario.getId(), usuario.getNombre(),
                usuario.getCorreoElectronico(), usuario.getPerfil().getId());
    }

    @Transactional
    public UsuarioResponseDto actualizarUsuario(Long id, UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado con ID: " + id));

        if (!usuario.getCorreoElectronico().equals(usuarioRequestDto.correoElectronico())
                && usuarioRepository.existsByCorreoElectronico(usuarioRequestDto.correoElectronico())) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un usuario con este correo electrónico");
        }

        Perfil perfil = perfilRepository.findById(usuarioRequestDto.perfilId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "No se encontró el perfil con ID: " + usuarioRequestDto.perfilId()));

        usuario.setNombre(usuarioRequestDto.nombre());
        usuario.setCorreoElectronico(usuarioRequestDto.correoElectronico());

        if (usuarioRequestDto.contrasena() != null && !usuarioRequestDto.contrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioRequestDto.contrasena()));
        }

        usuario.setPerfil(perfil);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponseDto(usuarioActualizado.getId(), usuarioActualizado.getNombre(),
                usuarioActualizado.getCorreoElectronico(), usuarioActualizado.getPerfil().getId());
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
