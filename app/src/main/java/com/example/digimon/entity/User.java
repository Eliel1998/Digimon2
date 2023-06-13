package com.example.digimon.entity;

public class User {
    private String userName;
    private Long score;

    public User() {
    }

    public User(String userName, Long score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}

