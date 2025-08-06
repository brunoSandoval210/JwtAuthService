package com.apirest.JwtAuthService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "JWT Authentication Service API",
                description = "Este proyecto proporciona un servicio de autenticación basado en JSON Web Tokens (JWT) implementado en Spring Boot. Su principal objetivo es permitir el inicio de sesión seguro mediante credenciales de usuario, generando un token JWT que puede ser utilizado para acceder a rutas protegidas dentro de una arquitectura de microservicios o aplicaciones backend.\n" +
                        "\n" +
                        "La autenticación se maneja mediante un filtro personalizado, el cual intercepta la ruta /auth/login, valida las credenciales, y devuelve un token firmado en caso de éxito. El sistema también maneja respuestas estructuradas para errores de autenticación o credenciales inválidas.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Bruno Sandoval",
                        email = "bruno.sandoval210@gmail.com"
//                        url = "https://github.com/brunoSandoval210"
                )
//                license = @License(
//                        name = "Standard Software License for Bruno Sandoval",
//                        url = "https://github.com/brunoSandoval210"
//                )
        ),
        servers = {
                @Server(
                        description = "Development Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Server",
                        url = "https://jwtauthservice.onrender.com/"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Acces token for protected routes",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
