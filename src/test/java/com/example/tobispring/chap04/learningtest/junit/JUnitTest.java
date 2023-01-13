package com.example.tobispring.chap04.learningtest.junit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;
import static org.hamcrest.CoreMatchers.not;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("junit.xml")
@SpringBootTest
public class JUnitTest {
    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertSame(testObjects, this);
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test public void test2() {
        assertSame(testObjects, this);
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test public void test3() {
        assertSame(testObjects, this);
        testObjects.add(this);

//        assertSame(contextObject, either(is(nullValue())).or(is(this.contextObject)));
//        contextObject = this.context;
    }
}

