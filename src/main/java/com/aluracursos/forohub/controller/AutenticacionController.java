package com.aluracursos.forohub.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.aluracursos.forohub.entity.Usuario;
import com.aluracursos.forohub.infra.security.DatosTokenJWT;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import com.aluracursos.forohub.dto.DatosAutenticacion;
import com.aluracursos.forohub.infra.security.TokenService;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity<DatosTokenJWT> iniciarSesion(@RequestBody @Valid DatosAutenticacion datos) {
        try {
            var autheneticationToken = new UsernamePasswordAuthenticationToken(datos.login(),
                    datos.contrasena());
            var autenticacion = manager.authenticate(autheneticationToken);
            var usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
            var tokenJWT = tokenService.generarToken(usuarioAutenticado);

            return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
    }
}
