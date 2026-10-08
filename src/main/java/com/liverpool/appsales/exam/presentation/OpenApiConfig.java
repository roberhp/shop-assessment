package com.liverpool.appsales.exam.presentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestión de Pedidos - Liverpool",
                version = "1.0.0",
                description = "API REST para la gestión de clientes, pedidos, entregas "
                        + "y búsqueda de información de pedidos y productos.",
                contact = @Contact(
                        name = "Equipo de desarrollo"
                )
        )
)
public class OpenApiConfig {
}