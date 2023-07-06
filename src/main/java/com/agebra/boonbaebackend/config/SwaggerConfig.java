package com.agebra.boonbaebackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "분배법칙 백엔드 API 명세서",
    description = "분배법칙 백엔드 API 명세입니다",
    version = "v1"
  ),
        servers = {
                @Server(url = "http://localhost:80", description = "Local 테스트용 서버"),
                @Server(url = "https://boonbae.seol.pro", description = "AWS 서버")
        }
)
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI(){
    SecurityScheme securityScheme = new SecurityScheme()
      .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
      .in(SecurityScheme.In.HEADER).name("Authorization");
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

    return new OpenAPI()
      .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
      .security(Arrays.asList(securityRequirement));
  }
}
