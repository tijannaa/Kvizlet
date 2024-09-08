package com.example.KvizletApi.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user") // Ovo dodaje kolonu user u tabeli Pitanja
    private List<Pitanje> quizQuestions;
}
