// package com.aluracursos.forohub.infra.exceptions;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.bind.MethodArgumentNotValidException;

// import jakarta.persistence.EntityNotFoundException;

// @RestControllerAdvice
// public class GestorDeErrores {

// @ExceptionHandler(EntityNotFoundException.class)
// public ResponseEntity gestionarError404() {
// return ResponseEntity.notFound().build();
// }

// @ExceptionHandler(MethodArgumentNotValidException.class)
// public ResponseEntity gestionarError400(MethodArgumentNotValidException ex) {
// var errores = ex.getFieldErrors();
// return ResponseEntity.badRequest().body(errores.stream()
// .map(DatosErrorValidacion::new)
// .toList());
// }

// @ExceptionHandler(ValidacionException.class)
// public ResponseEntity gestionarErrorDeValidacion(ValidacionException e) {
// return ResponseEntity.badRequest().body(e.getMessage());

// }

// @ExceptionHandler(BadCredentialsException.class)
// public ResponseEntity gestionarErrorBadCredentials() {
// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales
// inválidas");
// }

// @ExceptionHandler(AuthenticationException.class)
// public ResponseEntity gestionarErrorAuthentication() {
// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falla en la
// autenticación");
// }

// @ExceptionHandler(AccessDeniedException.class)
// public ResponseEntity gestionarErrorAccesoDenegado() {
// return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
// }

// @ExceptionHandler(Exception.class)
// public ResponseEntity gestionarError500(Exception ex) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "
// + ex.getLocalizedMessage());
// }

// @ExceptionHandler(HttpMessageNotReadableException.class)
// public ResponseEntity gestionarErrorCuerpoFaltante() {
// return ResponseEntity.badRequest().body(new DatosErrorValidacion(
// "request body",
// "El cuerpo de la solicitud es requerido o está mal formado"));
// }

// public record DatosErrorValidacion(
// String campo,
// String mensaje) {

// public DatosErrorValidacion(FieldError error) {
// this(error.getField(), error.getDefaultMessage());
// }
// }

// }
