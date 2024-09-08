package com.example.KvizletApi.Controllers;

import com.example.KvizletApi.Entities.Pitanje;
import com.example.KvizletApi.Services.PitanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class PitanjeController {
    @Autowired
    private PitanjeService pitanjeService;

    @GetMapping("/all/{username}")
    public List<Pitanje> getAllQuestions(@PathVariable String username) {
        return pitanjeService.getAllQuestions(username);
    }

    @GetMapping("/{id}")
    public Pitanje getQuestionById(@PathVariable Long id) {
        return pitanjeService.getQuestionById(id);
    }

    @PostMapping
    public Pitanje createQuestion(@RequestBody Pitanje quizQuestion) {
        return pitanjeService.saveQuestion(quizQuestion);
    }

    @PutMapping("/{id}")
    public Pitanje updateQuestion(@PathVariable Long id, @RequestBody Pitanje quizQuestion) {
        return pitanjeService.updateQuestion(id, quizQuestion);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        pitanjeService.deleteQuestion(id);
    }
}
