package com.question.questionservice.service;

import com.question.questionservice.model.Question;
import com.question.questionservice.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository repository;

    @Override
    public Question add(Question question) {
        return repository.save(question);
    }

    @Override
    public List<Question> getAll() {
        return repository.findAll();
    }

    @Override
    public Question getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Question not found."));
    }

    @Override
    public List<Question> getQuestionsOfQuiz(Long quizId) {
        return repository.findByQuizId(quizId);
    }
}
