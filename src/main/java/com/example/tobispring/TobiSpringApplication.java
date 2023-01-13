package com.example.tobispring;

import com.example.tobispring.chap05.DaoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DaoFactory.class)
public class TobiSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(TobiSpringApplication.class, args);
    }
}
