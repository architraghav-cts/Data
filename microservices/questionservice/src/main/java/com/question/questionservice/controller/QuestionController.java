package com.question.questionservice.controller;

import com.question.questionservice.model.Question;
import com.question.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @PostMapping
    public Question add(@RequestBody Question question) {
        return service.add(question);
    }

    @GetMapping("/all")
    public List<Question> getAll() {
        return service.getAll();
    }

    @GetMapping("/id/{id}")
    public Question getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @GetMapping("/quiz/{quizId}")
    public List<Question> getQuestionOfQuiz(@PathVariable("quizId") Long quizId) {
        return service.getQuestionsOfQuiz(quizId);
    }
}