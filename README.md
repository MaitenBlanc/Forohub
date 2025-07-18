# FOROHUB

ForoHub es una API RESTful construida con Java y Spring Boot, que simula un foro de discusión. Implementa autenticación con JWT, operaciones CRUD sobre tópicos y persistencia de datos en MySQL.

## Características

- Creación de hilos y publicaciones.
- Búsqueda avanzada de contenido.
- Interacción mediante comentarios y reacciones.
- Perfiles de usuario personalizables.
- Categorización por categorías.
- Autenticación segura con JWT.

## Tecnologías Utilizadas

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Maven
- MySQL
- Flyway
- Lombok
- JPA
- Validation

## Instalación Local

### Requisitos Previos

- Java JDK (v17+)
- Maven (v4+)
- Spring Boot (v3+)
- MySQL(v8+)
- Dependencias
- IDE (VS Code o IntelliJ IDEA - recomendados)

### Configuración del entorno

1. Crear el proyecto con Spring Initializr:

   - https://start.spring.io/
   - Tipo: Maven Project
   - Packaging: JAR
   - Java: 17

2. Clonar el repositorio:

   ```bash
   git clone git@github.com:MaitenBlanc/Forohub.git
   cd Forohub
   ```

3. Configurar la base de datos en src//main/resources/application.properties:
   spring.datasource.url=jdbc:mysql://localhost:3306/forohub
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=validate
   spring.jpa.show-sql=true
   spring.flyway.enabled=true
   jwt.secret=tu_clave_secreta_segura

4. Crear la base de datos forohub en MySQL antes de ejecutar la app.

## Funcionalidades

### Endpoints disponibles

    **Método**	        **URI**	                **Descripción**
    POT                 /login                  Autenticación de usuario
    POST	            /topicos	            Crear un nuevo tópico
    GET	                /topicos	            Listar todos los tópicos
    GET	                /topicos/{id}	        Detallar un tópico específico
    PUT	                /topicos/{id}	        Actualizar un tópico existente
    DELETE	            /topicos/{id}	        Eliminar un tópico por su ID

    Nota: Todos los endpoints (excepto /login) requieren autenticación mediante JWT.

### Autenticación con JWT

La API implementa seguridad basada en tokens JWT con Spring Security. Solo los usuarios autenticados pueden interactuar con los recursos protegidos.

#### Proceso

1. Realizá una solicitud POST a /login con:
   {
   "login": "usuario",
   "password": "contraseña"
   }

2. Recibirás un token en la respuesta. Usalo en los siguientes requests:
   Authorization: Bearer TU_TOKEN

3. El token se valida mediante un filtro personalizado.

#### Claves de seguridad

-Generación y validación del token con TokenService usando HMAC256.
-El token incluye tiempo de expiración (jwt.expiration).
-Seguridad configurada en SecurityConfigurations usando HttpSecurity.

## Reglas de negocio

- Todos los campos del tópico son obligatorios.
- No se permiten tópicos duplicados (mismo título y mensaje).
- Se utiliza validación con anotaciones @Valid.
- Control de integridad usando Optional.isPresent() y JpaRepository.

## Testing

Podés testear los endpoints usando herramientas como:

- Postman
- Insomnia

## Estructura de la Base de Datos

- Tabla principal: topicos

* id
* titulo
* mensaje
* fechaCreacion
* status
* autor
* curso

- Tabla de usuarios (para autenticación)

* id
* login
* password (encriptado)

## Licencia

MIT © Maiten Blanc

## Créditos y recursos

- Proyecto basado en prácticas recomendadas de Alura Latam.

- Documentación útil:
  - Spring Data JPA
  - Spring Security
  - JWT.io
  - Baeldung - Spring Boot
