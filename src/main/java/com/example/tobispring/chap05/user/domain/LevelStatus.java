package com.example.tobispring.chap05.user.domain;

import java.util.Objects;

public class LevelStatus {
    public static final int UPGRADE_LOGIN_COUNT_LOWER_BOUND = 50;
    public static final int UPGRADE_RECOMMEND_COUNT_LOWER_BOUND = 30;

    private int login;
    private int recommend;

    public LevelStatus() {
    }

    public LevelStatus(int login, int recommend) {
        this.login = login;
        this.recommend = recommend;
    }

    public boolean isOverUpgradeLoginCount() {
        return login >= UPGRADE_LOGIN_COUNT_LOWER_BOUND;
    }

    public boolean isOverUpgradeRecommendCount() {
        return recommend >= UPGRADE_RECOMMEND_COUNT_LOWER_BOUND;
    }

    // 개판;
    public void setLogin(int login) {
        this.login = login;
    }
    // 개판;
    public int getLogin() {
        return login;
    }
    // 개판;
    public int getRecommend() {
        return recommend;
    }
    // 개판;
    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LevelStatus that = (LevelStatus) o;
        return login == that.login && recommend == that.recommend;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, recommend);
    }
}
