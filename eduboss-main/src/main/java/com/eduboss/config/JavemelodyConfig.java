package com.eduboss.config;

import net.bull.javamelody.MonitoringSpringAdvisor;

import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//@Configuration
public class JavemelodyConfig {
    
    @Bean
    public MonitoringSpringAdvisor springServiceMonitoringAdvisor() {
        final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
        interceptor.setPointcut(new AnnotationMatchingPointcut(Service.class));
        return interceptor;
    }

    @Bean
    public MonitoringSpringAdvisor springControllerMonitoringAdvisor() {
        final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
        interceptor
                .setPointcut(new AnnotationMatchingPointcut(Controller.class));
        return interceptor;
    }

    
    @Bean
    public MonitoringSpringAdvisor springRepositoryMonitoringAdvisor() {
        final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
        interceptor
                .setPointcut(new AnnotationMatchingPointcut(Repository.class));
        return interceptor;
    }

}
