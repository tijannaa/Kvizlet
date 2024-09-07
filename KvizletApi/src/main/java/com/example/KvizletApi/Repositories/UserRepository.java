package com.example.KvizletApi.Repositories;

import com.example.KvizletApi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}