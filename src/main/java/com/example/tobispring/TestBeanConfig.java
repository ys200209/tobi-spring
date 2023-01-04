package com.example.tobispring;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TestBeanConfig {
    @Bean
    public TestBean testBean() {
        return new TestBean();
    }
}
