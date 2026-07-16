package com.quiz.quizservice.controller;

import com.quiz.quizservice.model.Quiz;
import com.quiz.quizservice.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private QuizService service;

    @PostMapping
    public Quiz add(@RequestBody Quiz quiz) {
        return service.add(quiz);
    }

    @GetMapping("/all")
    public List<Quiz> getAll() {
        return service.getAll();
    }

    @GetMapping("/id/{id}")
    public Quiz getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }
}