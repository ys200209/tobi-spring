package com.example.tobispring.chap03;

import com.example.tobispring.chap01.DaoFactory;
import com.example.tobispring.chap01.UserDao;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
//@ContextConfiguration()
class UserDaoTest {
    @Autowired UserDao userDao;
    @Autowired DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new DaoFactory().userDao();
        userDao.deleteAll();
    }

    @RepeatedTest(10)
    void testA() throws SQLException {
        // when
        userDao.runMain();
    }

}