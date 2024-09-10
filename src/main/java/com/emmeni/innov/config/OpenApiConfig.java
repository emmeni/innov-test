package com.emmeni.innov.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		contact = @Contact(name = "Emmeni Emmanuel", email = "bag.emmaus@gmail.com", url = "https://www.linkedin.com/in/e-emmeni/"),
		description = "Innov Documentation for ticket management API",
		title = "Innov API specification",
		license = @License(name = "MIT License"),
		version = "0.0.1"
	),
	servers = @Server(url = "http://localhost:8081", description = "Innov host server url"),
	security = { @SecurityRequirement(name = "bearerAuth") }
)
@SecurityScheme(
	name = "bearerAuth", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
	
}
