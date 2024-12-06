package com.qualitas.portal.accountfraudesapi.configuracion.cors;

import com.qualitas.portal.accountfraudesapi.configuracion.permitido.HostsPermitidos;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class RestConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addCorsMappings(CorsRegistry registry){

        List<String> originsAllowed = HostsPermitidos.obtenerListaHostPermitidos();

        registry.addMapping("/**").
                allowedOrigins("*").
                allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE").
                allowedHeaders("Authorization", "Content-Type", "Accept", "X-Requested-With").
                allowCredentials(true);
    }

}
