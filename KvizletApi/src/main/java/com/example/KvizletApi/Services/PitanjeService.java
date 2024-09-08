package com.example.KvizletApi.Services;
import com.example.KvizletApi.Entities.Pitanje;
import com.example.KvizletApi.Entities.User;
import com.example.KvizletApi.Repositories.PitanjeRepository;
import com.example.KvizletApi.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PitanjeService {

    @Autowired
    private PitanjeRepository pitanjeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Pitanje> getAllQuestions(String username) {
        return pitanjeRepository.findByUsername(username);
    }

    public Pitanje getQuestionById(Long id) {
        return pitanjeRepository.findById(id).orElse(null);
    }

    public Pitanje saveQuestion(Pitanje quizQuestion) {
        User user = userRepository.findByUsername(quizQuestion.getUsername());

        if (user == null) {
            throw new RuntimeException("User not found with username: " + quizQuestion.getUsername());
        }

        user.getQuizQuestions().add(quizQuestion);

        userRepository.save(user);

        return quizQuestion;
    }

    public void deleteQuestion(Long id) {
        pitanjeRepository.deleteById(id);
    }

    public Pitanje updateQuestion(Long id, Pitanje updatedQuestion) {
        Optional<Pitanje> optionalQuestion = pitanjeRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Pitanje existingQuestion = optionalQuestion.get();
            existingQuestion.setQuestion(updatedQuestion.getQuestion());
            existingQuestion.setAnswer(updatedQuestion.getAnswer());
            return pitanjeRepository.save(existingQuestion);
        }
        return null;
    }
}
