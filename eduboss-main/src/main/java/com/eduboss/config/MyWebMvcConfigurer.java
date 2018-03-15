package com.eduboss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.eduboss.interceptor.ForbiddenRecommitInterceptor;


@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ForbiddenRecommitInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}