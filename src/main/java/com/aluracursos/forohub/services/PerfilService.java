package com.aluracursos.forohub.services;

import com.aluracursos.forohub.dto.PerfilRequestDto;
import com.aluracursos.forohub.dto.PerfilResponseDto;
import com.aluracursos.forohub.entity.Perfil;
import com.aluracursos.forohub.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Transactional
    public PerfilResponseDto registrarPerfil(PerfilRequestDto perfilRequestDto) {
        Perfil perfil = new Perfil();
        perfil.setNombre(perfilRequestDto.nombre());

        Perfil perfilGuardado = perfilRepository.save(perfil);
        return new PerfilResponseDto(perfilGuardado.getId(), perfilGuardado.getNombre());
    }

    @Transactional(readOnly = true)
    public Page<PerfilResponseDto> listarPerfiles(Pageable paginacion) {
        return perfilRepository.findAll(paginacion)
                .map(perfil -> new PerfilResponseDto(perfil.getId(), perfil.getNombre()));
    }

    @Transactional(readOnly = true)
    public PerfilResponseDto obtenerPerfilPorId(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        return new PerfilResponseDto(perfil.getId(), perfil.getNombre());
    }

    @Transactional
    public PerfilResponseDto actualizarPerfil(Long id, PerfilRequestDto perfilRequestDto) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        perfil.setNombre(perfilRequestDto.nombre());
        Perfil perfilActualizado = perfilRepository.save(perfil);
        return new PerfilResponseDto(perfilActualizado.getId(), perfilActualizado.getNombre());
    }

    @Transactional
    public void eliminarPerfil(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new RuntimeException("Perfil no encontrado");
        }
        perfilRepository.deleteById(id);
    }
}
