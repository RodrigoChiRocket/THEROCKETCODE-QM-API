package com.qualitas.portal.accountfraudesapi.configuracion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class FileUploadConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(50 * 1024 * 1024); // 50MB
        resolver.setMaxInMemorySize(4096); // 4KB antes de usar temp file
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }
}