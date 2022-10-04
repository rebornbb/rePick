package com.gosari.repick_project.answer;

import com.gosari.repick_project.answer.Answer;
import com.gosari.repick_project.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository <Answer, Integer>{
    Page<Answer> findAllByQuestion(Question question, Pageable pageable);
}
