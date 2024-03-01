package com.jhm.springbootwebservice.config;

import jakarta.servlet.MultipartConfigElement;
import lombok.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartConfig {

//    @Value("${file.multipart.maxUploadSize:104857600}")
    private long maxUploadSize;

//    @Value("${file.multipart.maxUploadSizePerFile:10485760}")
    private long maxUploadSizePerFile;

    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        return multipartResolver;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofBytes(10485760));
        factory.setMaxFileSize(DataSize.ofBytes(10485760));

        return factory.createMultipartConfig();
    }
}