package com.example.tobispring.chap06;

import com.example.tobispring.chap06.user.aop.NameMatchClassMethodPointcut;
import com.example.tobispring.chap06.user.aop.TransactionAdvice;
import com.example.tobispring.chap06.user.dao.UserDao;
import com.example.tobispring.chap06.user.dao.UserDaoJdbc;
import com.example.tobispring.chap06.user.service.UserService;
import com.example.tobispring.chap06.user.service.UserServiceImpl;
import javax.sql.DataSource;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AppConfigChap06 {
    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setDataSource(dataSource());
        return userDaoJdbc;
    }

    @Bean
    public PlatformTransactionManager transactionManager() { // 트랜잭션 매니저 빈
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public TransactionAdvice transactionAdvice() { // 트랜잭션 어드바이스 빈
        TransactionAdvice transactionAdvice = new TransactionAdvice();
        transactionAdvice.setTransactionManager(transactionManager());
        return transactionAdvice;
    }

    @Bean
    public NameMatchClassMethodPointcut transactionPointcut() { // 트랜잭션 포인트 컷 빈 (upgrade로 시작하는 메서드)
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl"); // 클래스 이름 패턴
        pointcut.setMappedName("upgrade*"); // 메소드 이름 패턴
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() { // 트랜잭션 어드바이저 (포인트 컷 + 어드바이스)
        return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    }

    @Bean
    public UserService userServiceImpl() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        return userService;
    }

    /*@Bean
    public ProxyFactoryBean userService() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(userServiceImpl());
        proxyFactoryBean.setInterceptorNames("transactionAdvisor");
        return proxyFactoryBean;
    }*/

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/tobi?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }
}
