package com.unla.grupo16.configurations.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**") // aplica a todo /api/**
                .allowedOrigins("http://localhost:3000") // solo permite peticiones de este origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // metodos http permitidos
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
