package com.example.KvizletApi.Repositories;

import com.example.KvizletApi.Entities.Pitanje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PitanjeRepository  extends JpaRepository<Pitanje, Long> {
    List<Pitanje> findByUsername(String username);
}