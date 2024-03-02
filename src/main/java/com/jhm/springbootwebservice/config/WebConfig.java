package com.jhm.springbootwebservice.config;

import com.jhm.springbootwebservice.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 정적인 리소스에 대한 요청을 처리하는 핸들러, 정적 파일들의 경로를 잡아주는 메서드
        registry.addResourceHandler("/**")
            .addResourceLocations("file:src/main/resources/static/");
//        addResourceHandler에 정의한 루트로 들어오는 모든 정적 리소스 요청을
//        addResourceLocations에서 정의한 경로에서 찾는다는 의미이다.
    }
}
