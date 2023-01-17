package com.example.tobispring.chap05.user.dao;

import com.example.tobispring.chap05.user.domain.Level;
import com.example.tobispring.chap05.user.domain.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

//@Component
public class UserDaoJdbc implements UserDao {
	public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM USERS WHERE id = ?";
	public static final String SELECT_COUNT_QUERY = "SELECT COUNT(*) FROM USERS";
	public static final String SELECT_USERS_ORDER_BY_ID_QUERY = "SELECT * FROM USERS ORDER BY id";
	public static final String INSERT_USER_QUERY = "INSERT INTO USERS(id, name, password, level, login, recommend)"
			+ " VALUES (?, ?, ?, ?, ?, ?)";
	public static final String DELETE_USER_QUERY = "DELETE FROM USERS";
	public static final String UPDATE_USER_QUERY = "UPDATE USERS SET name = ?, password = ?, level = ?,"
			+ "login = ?, recommend = ? WHERE id = ?";

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper =
			(rs, rowNum) -> {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		};


	public void add(final User user) {
		this.jdbcTemplate.update(INSERT_USER_QUERY,
				user.getId(), user.getName(), user.getPassword(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject(SELECT_USER_BY_ID_QUERY,
				new Object[] {id}, this.userMapper);
	} 

	public void deleteAll() {
		this.jdbcTemplate.update(DELETE_USER_QUERY);
	}

	public int getCount() {
		return jdbcTemplate.queryForObject(SELECT_COUNT_QUERY, Integer.class);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query(SELECT_USERS_ORDER_BY_ID_QUERY, this.userMapper);
	}

	@Override
	public void update(User updateUser) {
		this.jdbcTemplate.update(UPDATE_USER_QUERY,
				updateUser.getName(), updateUser.getPassword(), updateUser.getLevel().intValue(),
				updateUser.getLogin(), updateUser.getRecommend(), updateUser.getId());
	}
}
