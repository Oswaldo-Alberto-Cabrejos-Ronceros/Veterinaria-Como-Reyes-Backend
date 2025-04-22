package com.veterinaria.veterinaria_comoreyes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Veterinaria Como Reyes",
                version="1.0",
                description = "Documentacion de la API para el sistema de gestión veterinaria"
        )
)

@Configuration
public class SwaggerConfig {
}
