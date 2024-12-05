package github.amanand2507.journalApp.config;

import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("all-apis")
                .pathsToMatch("/**") // Match all paths
                .addOpenApiCustomiser(
                        openApi -> openApi.addSecurityItem(new SecurityRequirement().addList("bearer-token")))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Journal API")
                        .version("1.0")
                        .description("API documentation for Journal Application"))
                .components(new Components()
                        .addSecuritySchemes("bearer-token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // Specifies JWT format for the token
                        ))
                .addServersItem(new Server().url("http://localhost:8080")
                        .description("Local Host Server"))
                .addServersItem(new Server().url("https://journalapp-spds.onrender.com")
                        .description("Production Server"));  // Add your production server URL here
    }
}
