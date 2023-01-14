package com.example.tobispring.chap05.user.dao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.example.tobispring.chap05.AppConfigChap05;
import com.example.tobispring.chap05.user.dao.UserDao;
import com.example.tobispring.chap05.user.domain.Level;
import com.example.tobispring.chap05.user.domain.User;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/test-applicationContext.xml")
@SpringBootTest(classes = AppConfigChap05.class)
public class UserDaoTest {
    @Autowired
    UserDao dao;
    @Autowired
    DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("gyumee", "이름 1", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("leegw700", "이름 2", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "이름 3", "springno3", Level.GOLD, 100, 40);
    }

    @Test
    public void andAndGet() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    @Test
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);


        assertThatThrownBy(() -> dao.get("unknown_id"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void count() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        assertEquals(dao.getCount(), 1);

        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        dao.add(user3);
        assertEquals(dao.getCount(), 3);
    }

    @Test
    public void getAll()  {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertEquals(users0.size(), 0);

        dao.add(user1); // Id: gyumee
        List<User> users1 = dao.getAll();
        assertEquals(users1.size(), 1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2); // Id: leegw700
        List<User> users2 = dao.getAll();
        assertEquals(users2.size(), 2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3); // Id: bumjin
        List<User> users3 = dao.getAll();
        assertEquals(users3.size(), 3);
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        assertThatThrownBy(() -> dao.add(user1))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();

        dao.add(user1);

        assertThatThrownBy(() -> dao.add(user1))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void update() {
        // given
        dao.deleteAll();
        dao.add(user1);
        dao.add(user2);

        // when
        user1.setName("변경된 이름");
        user1.setPassword("변경된 패스워드");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        // then
        User updateUser = dao.get(user1.getId());
        assertEquals(user1, updateUser);
        user1.equals(updateUser);
        User notUpdateUser = dao.get(user2.getId());
        assertEquals(user2, notUpdateUser);
    }
}
