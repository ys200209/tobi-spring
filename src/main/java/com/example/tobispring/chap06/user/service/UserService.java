package com.example.tobispring.chap06.user.service;

import com.example.tobispring.chap06.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
