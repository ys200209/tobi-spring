package com.example.tobispring.chap05;

public class User {
    String id;
    String name;
    String password;

    Level level;

    int login;
    int recommend;
    public User() {
    }


    /*public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }*/
    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public int getRecommend() {
        return recommend;
    }
}
