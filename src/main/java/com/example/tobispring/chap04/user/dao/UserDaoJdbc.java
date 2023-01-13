package com.example.tobispring.chap04.user.dao;

import com.example.tobispring.chap04.user.domain.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

//@Component
public class UserDaoJdbc implements UserDao {
	public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM USERS WHERE id = ?";
	public static final String SELECT_COUNT_QUERY = "SELECT COUNT(*) FROM USERS";
	public static final String SELECT_USERS_ORDER_BY_ID_QUERY = "SELECT * FROM USERS ORDER BY id";
	public static final String INSERT_USER_QUERY = "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)";
	public static final String DELETE_USER_QUERY = "DELETE FROM USERS";

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper =
		new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				return user;
			}
		};

	
	public void add(final User user) {
		this.jdbcTemplate.update(INSERT_USER_QUERY,
						user.getId(), user.getName(), user.getPassword());
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
}
