package com.example.kvizletandroid.models;

import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Pitanje> quizQuestions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Pitanje> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<Pitanje> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }
}
