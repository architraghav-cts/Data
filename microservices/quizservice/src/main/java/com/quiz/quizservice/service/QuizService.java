package com.quiz.quizservice.service;

import com.quiz.quizservice.model.Quiz;

import java.util.List;

public interface QuizService {
    Quiz add(Quiz quiz);

    List<Quiz> getAll();

    Quiz getById(Long id);
}