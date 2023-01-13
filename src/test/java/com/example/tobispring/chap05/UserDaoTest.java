package com.example.tobispring.chap05;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.tobispring.AppConfig;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest(classes = AppConfig.class)
//@ContextConfiguration()
class UserDaoTest {
    @Autowired
    UserDao userDao;
    @Autowired
    DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        /*userDao = new DaoFactory().userDao();
        userDao.deleteAll();*/

        this.user1 = new User("kim", "김", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("lee", "이", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("park", "박", "springno3", Level.GOLD, 100, 40);
    }

    /*@RepeatedTest(10)
    void testA() throws SQLException {
        // when
        userDao.runMain();
    }*/

    @Test
    void testSpringTestContext() {
        // given
        ApplicationContext config = new AnnotationConfigApplicationContext(DaoFactory.class);

        // when
        UserDao userDao = config.getBean("userDao", UserDao.class);
        System.out.println(userDao);

        // then
        assertNotNull(userDao);
    }

    @DisplayName("addAndGet")
    @Test
    void testAddAndGet() {
        // given
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
        userDao.setDataSource(dataSource);

        userDao.deleteAll();

        User addUser1 = new User("id1", "name1", "password1", Level.BASIC, 0, 1);
        User addUser2 = new User("id2", "name2", "password2", Level.SILVER, 1, 2);

        // when
        userDao.add(addUser1);
        userDao.add(addUser2);
        assertEquals(userDao.getCount(), 2);

        // then
        User getUser1 = userDao.get(addUser1.getId());
        checkSameUser(getUser1, addUser1);

        User getUser2 = userDao.get(addUser2.getId());
        checkSameUser(getUser2, addUser2);
    }

    @DisplayName("getAll")
    @Test
    void testGetAll() {
        userDao.deleteAll();

        // assert 1
        userDao.add(user1); // Id: kim
        List<User> users1 = userDao.getAll();
        assertEquals(users1.size(), 1);
        checkSameUser(user1, users1.get(0));

        // assert 2
        userDao.add(user2); // Id: lee
        List<User> users2 = userDao.getAll();
        assertEquals(users2.size(), 2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        // assert 2
        userDao.add(user3); // Id: park
        List<User> users3 = userDao.getAll();
        assertEquals(users3.size(), 3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        // then
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }
}
