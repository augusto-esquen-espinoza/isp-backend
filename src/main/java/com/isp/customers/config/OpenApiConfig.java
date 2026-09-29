package com.isp.customers.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger metadata for the customers microservice.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customersOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ISP Customers API")
                        .description("Microservice for customer registration and birth indicators")
                        .version("1.0.0")
                        .contact(new Contact().name("ISP")));
    }
}
