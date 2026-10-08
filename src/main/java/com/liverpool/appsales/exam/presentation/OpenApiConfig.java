package com.liverpool.appsales.exam.presentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Liverpool Order Management API",
                version = "1.0.0",
                description = "REST API for customer, order, delivery and order search management.",
                contact = @Contact(
                        name = "Backend Technical Assessment"
                )
        )
)
public class OpenApiConfig {
}