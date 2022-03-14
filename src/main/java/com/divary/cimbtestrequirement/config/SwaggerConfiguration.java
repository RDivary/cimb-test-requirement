package com.divary.cimbtestrequirement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    private static final String AUTHORIZATION = "Authorization";

    public Docket getBaseDocket() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .build();
    }

    @Bean
    public Docket docket() {
        return getBaseDocket()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(AUTHORIZATION, authorizationScopes));

        return securityReferences;
    }
}