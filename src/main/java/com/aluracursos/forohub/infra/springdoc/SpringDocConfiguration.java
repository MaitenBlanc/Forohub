package com.aluracursos.forohub.infra.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfiguration {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("ForoHub API")
                                                .version("1.0.0")
                                                .description("""
                                                                API REST para ForoHub, un sistema de foros de discusión y aprendizaje.
                                                                <br><br>
                                                                Incluye gestión de:
                                                                <ul>
                                                                        <li>Tópicos de discusión</li>
                                                                        <li>Respuestas a tópicos</li>
                                                                        <li>Usuarios y perfiles</li>
                                                                        <li>Cursos y categorías</li>
                                                                </ul>
                                                                """)
                                                .contact(new Contact()
                                                                .name("Equipo de ForoHub")
                                                                .email("soporte@forohub.com")
                                                                .url("https://github.com/MaitenBlanc/Forohub"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                                .components(new Components()
                                                .addSecuritySchemes("bearer-key",
                                                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description("""
                                                                                                Autenticación JWT. Obtenga el token desde el endpoint /login.
                                                                                                <br>
                                                                                                Ejemplo: `Bearer {token}`
                                                                                                """)));
        }
}
