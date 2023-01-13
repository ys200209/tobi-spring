package com.example.tobispring.chap03;

import com.example.tobispring.chap03.connection.ConnectionMaker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDao {
    public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM USERS WHERE id = ?";
    public static final String SELECT_COUNT_QUERY = "SELECT COUNT(*) FROM USERS";
    public static final String SELECT_USERS = "SELECT * FROM USERS ORDER BY id";
    public static final String INSERT_USER_QUERY = "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)";
    public static final String DELETE_USER_QUERY = "DELETE FROM USERS";

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    public static void main(String[] args) throws SQLException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("seyeong");
        user.setName("이");
        user.setPassword("lee");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

    public void runMain() throws SQLException {
        ApplicationContext config = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = config.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("seyeong");
        user.setName("이");
        user.setPassword("lee");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

    public void add(User user) {
        jdbcTemplate.update(INSERT_USER_QUERY, user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(SELECT_USER_BY_ID_QUERY, new Object[]{id}, userMapper);
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_USER_QUERY);
    }

    public int getCount() {
        return jdbcTemplate.queryForObject(SELECT_COUNT_QUERY, Integer.class);
    }

    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_USERS, userMapper);
    }
}
