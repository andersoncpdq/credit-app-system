package com.acpdq.credit.app.system.configurations

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("creditappsystem-public")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
    }
}