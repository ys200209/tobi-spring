package com.example.tobispring.chap04.user.dao;

import com.example.tobispring.chap04.user.domain.User;
import java.util.List;

public interface UserDao {

	void add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();

}