package com.qualitas.portal.accountfraudesapi.configuracion;

import com.qualitas.portal.accountfraudesapi.configuracion.ratelimit.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/multicotizador-api/**") // Aplica el rate limiting a todas las rutas bajo /api/
                .excludePathPatterns("/api/auth/**"); // Excluye las rutas de autenticación
    }
} 