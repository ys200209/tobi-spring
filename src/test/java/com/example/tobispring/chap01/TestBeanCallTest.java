package com.example.tobispring.chap01;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tobispring.TestBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class TestBeanCallTest {
    @Test
    void testCallSingletonBean() {
        ApplicationContext config = new AnnotationConfigApplicationContext(TestBean.class);
        TestBean getTestBean1 = config.getBean("testBean", TestBean.class);
        TestBean getTestBean2 = config.getBean("testBean", TestBean.class);
        TestBean newTestBean1 = new TestBean();
        TestBean newTestBean2 = new TestBean();

        System.out.println("getTestBean1 = " + getTestBean1);
        System.out.println("getTestBean2 = " + getTestBean2);
        System.out.println("newTestBean1 = " + newTestBean1);
        System.out.println("newTestBean2 = " + newTestBean2);
        assertEquals(getTestBean1, getTestBean2);
    }

}