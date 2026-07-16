package com.question.questionservice.service;

import com.question.questionservice.model.Question;

import java.util.List;

public interface QuestionService {
    Question add(Question question);

    List<Question> getAll();

    Question getById(Long id);

    List<Question> getQuestionsOfQuiz(Long quizId);
}
