package com.example.tobispring.chap06.study.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Proxy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class DynamicProxyTest {
    @Test
    void simpleProxy() {
        // given
        Hello proxiedHello = (Hello) Proxy.newProxyInstance( // JDK Dynamic Proxy 생성
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));

        // then
        assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxiedHello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

    @Test
    void testProxyFactoryBean() {
        // given
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget()); // 타겟 설정
        factoryBean.addAdvice(new UppercaseAdvice()); // 부가기능을 담은 어드바이스 추가. (여러 개 가능)
        Hello proxiedHello = (Hello) factoryBean.getObject(); // 팩토리빈이므로 getObject()를 통해 '프록시'를 가져온다. (프록시 팩토리 빈이니깐. 일반 팩토리 빈이 아니라.)

        // then
        assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxiedHello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

    static class UppercaseAdvice implements MethodInterceptor { // == InvocationHandler 기능 동일
        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    void testPointcutAdvisor() {
        // given
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        // when
        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) factoryBean.getObject();

        // then
        assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY"); // advice 적용
        assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY"); // advice 적용
        assertEquals(proxiedHello.sayThankYou("Toby"), "Thank You Toby"); // advice 적용 대상 예외 (pointcut)
    }

    @Test
    void testClassNamePointcutAdvisor() {
        // given
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };

        // when
        classMethodPointcut.setMappedName("sayH*");

        // then
        checkAdvised(new HelloTarget(), classMethodPointcut, true); // 클래스 포인트컷 적용

        class HelloWorld extends HelloTarget {};
        checkAdvised(new HelloWorld(), classMethodPointcut, false); // 클래스 포인트컷 미적용

        class HelloToby extends HelloTarget {};
        checkAdvised(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdvised(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        if (adviced) {
            assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
            assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
            assertEquals(proxiedHello.sayThankYou("Toby"), "Thank You Toby");
        } else {
            assertEquals(proxiedHello.sayHello("Toby"), "Hello Toby");
            assertEquals(proxiedHello.sayHi("Toby"), "Hi Toby");
            assertEquals(proxiedHello.sayThankYou("Toby"), "Thank You Toby");
        }
    }
}
