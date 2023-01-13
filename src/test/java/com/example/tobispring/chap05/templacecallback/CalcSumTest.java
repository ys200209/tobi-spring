package com.example.tobispring.chap05.templacecallback;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.tobispring.chap03.templatecallback.Calculator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalcSumTest {
    private Calculator calculator;
    private String PATH;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        PATH = "C:\\Users\\Lee\\Desktop\\techcourse\\playground\\tobi-spring\\src\\main\\resources\\numbers.txt";
    }

    @Test
    void sumOfNumbers() throws IOException {
        // then
        assertEquals(10, calculator.calcSum(PATH));
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        // then
        assertEquals(24, calculator.calcMultiply(PATH));
    }

    @Test
    void concatenateStrings() throws IOException {
        // then
        assertEquals("1234", calculator.concatenate(PATH));
    }
}
