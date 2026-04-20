package com.assignment.hardship.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AppConfig {
    @Bean
    public OpenAPI hardshipOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hardship Application API")
                        .description("API for managing hardship applications")
                        .version("v1.0"));
    }
}
