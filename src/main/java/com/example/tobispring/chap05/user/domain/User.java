package com.example.tobispring.chap05.user.domain;

import java.util.Objects;

public class User {
    public static final int UPGRADE_LOGIN_COUNT_LOWER_BOUND = 50;
    public static final int UPGRADE_RECOMMEND_COUNT_LOWER_BOUND = 30;

    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public Level getLevel() {
        return level;
    }

    public int getLogin() {
        return login;
    }

    public int getRecommend() {
        return recommend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return login == user.login && recommend == user.recommend && Objects.equals(id, user.id)
                && Objects.equals(name, user.name) && Objects.equals(password, user.password)
                && level == user.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, level, login, recommend);
    }

    public boolean isSameLevel(Level expectedLevel) {
        return level == expectedLevel;
    }

    public boolean isNowBasicLevel() {
        return level == Level.BASIC;
    }

    public boolean isOverUpgradeLoginCount() {
        return login >= UPGRADE_LOGIN_COUNT_LOWER_BOUND;
    }

    public boolean isOverUpgradeRecommendCount() {
        return recommend >= UPGRADE_RECOMMEND_COUNT_LOWER_BOUND;
    }

    public void upgradeLevel() {
        if (level.isNextLevelNull()) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }
        level = level.nextLevel();
    }

    public boolean canUpgradeLevel() {
        return level.canUpgradeLevel(login, recommend);
    }
}
