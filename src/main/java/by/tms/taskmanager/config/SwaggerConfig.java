package by.tms.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Smart Task Manager documentation")
                        .description("Smart Task Manager")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
