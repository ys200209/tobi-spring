package com.example.tobispring.chap03;

import com.example.tobispring.chap03.connection.ConnectionMaker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {
    public static final String INSERT_USER_QUERY = "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)";
    public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM USERS WHERE id = ?";
    public static final String DELETE_USER_QUERY = "DELETE FROM USERS";
    public static final String SELECT_COUNT_QUERY = "SELECT COUNT(*) FROM USERS";

    private final ConnectionMaker connectionMaker;
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public UserDao() {
        DaoFactory factory = new DaoFactory();
        this.connectionMaker = factory.connectionMaker();
    }

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

    public void add(User user) throws SQLException {
        jdbcContext.workWithStatementStrategy(
                c -> {
                    PreparedStatement ps = c.prepareStatement(INSERT_USER_QUERY);
                    ps.setString(1, user.getId());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getPassword());
                    return ps;
                });
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(SELECT_USER_BY_ID_QUERY);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()) {
            rs.next();
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        if (user == null) {
            throw new EmptyResultDataAccessException("사용자를 찾을 수 없습니다.", 1);
        }

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql(DELETE_USER_QUERY);
        System.out.println("회원 삭제 성공");
    }



    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(SELECT_COUNT_QUERY);
            rs = ps.executeQuery();

            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}
