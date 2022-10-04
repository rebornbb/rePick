package com.gosari.repick_project.answer;

import com.gosari.repick_project.exception.DataNotFoundException;
import com.gosari.repick_project.question.Question;
import com.gosari.repick_project.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    /*답변생성*/
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();

        answer.setContent(content);
        answer.setCreateDate(new Date());
        answer.setQuestion(question);
        answer.setAuthor(author);

        this.answerRepository.save(answer);

        return answer;
    }

    /*답변조회*/
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    /*답변수정*/
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(new Date());
        this.answerRepository.save(answer);
    }

    /*답변삭제*/
    public void delete(Answer answer){
        this.answerRepository.delete(answer);
    }

    /*추천인저장*/
    public void vote(Answer answer, SiteUser siteUser){
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }

    /*답변페이징*/
    public Page<Answer> getList(Question question, int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 5);
        return this.answerRepository.findAllByQuestion(question, pageable);
    }


}
