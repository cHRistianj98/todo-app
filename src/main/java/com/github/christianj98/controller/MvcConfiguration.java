package com.github.christianj98.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

// Without this configuration interceptors won't work
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    // injects all possible interceptors managed by Spring!
    private Set<HandlerInterceptor> interceptors;

    public MvcConfiguration(final Set<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        interceptors.forEach(registry::addInterceptor);
    }
}
