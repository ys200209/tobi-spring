package com.example.tobispring.chap01.di;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TestBeanConfig {
    @Bean
    public TestBean testBean() {
        return new TestBean();
    }
}
