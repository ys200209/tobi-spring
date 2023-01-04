package com.example.tobispring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TddTest {
    @DisplayName("ApplicationContext study test")
    @Test
    void testApplicationReferenceGetBean() {
        // given
        ApplicationContext config = new AnnotationConfigApplicationContext();

        // when
        config.getBean("a");

        // then

    }
}
