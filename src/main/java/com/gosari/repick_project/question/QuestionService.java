package com.gosari.repick_project.question;
import com.gosari.repick_project.answer.Answer;
import com.gosari.repick_project.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.*;

import com.gosari.repick_project.exception.DataNotFoundException;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor //QuestionRepository 생성자 생성해줌
@Service
public class QuestionService {
    @Value("${ImgLocation}")
    private String imgLocation;

    private final QuestionRepository questionRepository;
    private final QuestionPageRepository questionPageRepository;

    public List<Question> getList(){
        return this.questionRepository.findAll();
    }

    /*Question 데이터 조사*/
    public Question getQuestion(Integer id){
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()){

            //조회수
            Question question1 = question.get();
            question1.setCountview(question1.getCountview() + 1);
            this.questionRepository.save(question1);
            return question1;
            //조회수끝

            //return question.get();
        }else{
            throw new DataNotFoundException("question not found");
        }
    }
;
    /*페이징*/
    public Page<Question> getList(int page, String kw){  //검색어:kw
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return this.questionRepository.findAllByKeyword(kw,  pageable);
        /*질문레파지토리의 Query 애너테이션 - findAllByKeyword 매서드를 사용하기 위해 수정 */
    }

    /*질문데이터를 저장하는 create메서드*/
    public void create(String subject, String content, SiteUser user, String category,
                       MultipartFile file) throws Exception{

//        String projectPath = System.getProperty("user.dir") + "\\\\src\\\\main\\\\resources\\\\static\\\\files";
        String projectPath = imgLocation;

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(new Date());
        q.setAuthor(user);
        q.setCategory(category);

        q.setFilename(fileName);
        q.setFilepath(projectPath + fileName);

        this.questionRepository.save(q);
    }

    /*질문데이터 수정*/
    public Question modify(Question question, String subject, String content, String category, MultipartFile file) throws Exception {
        String projectPath = imgLocation;

        if (file.getOriginalFilename().equals("")){
            //새 파일이 없을 때
            question.setFilename(question.getFilename());
            question.setFilepath(question.getFilepath());

        } else if (file.getOriginalFilename() != null){
            //새 파일이 있을 때
            File f = new File(question.getFilepath());

            if (f.exists()) { // 파일이 존재하면
                f.delete(); // 파일 삭제
            }

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            question.setFilename(fileName);
            question.setFilepath(projectPath + fileName);
        }

        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(new Date());
        question.setCategory(category);

        this.questionRepository.save(question);

        return question;
    }

    /*질문데이터 삭제*/
    public void delete(Question question){
        this.questionRepository.delete(question);
    }

    /*추천인 저장*/
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    /*검색 메서드*/
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    /*이전,다음페이지*/
    public QuestionPage getQuestionByPageId(Question question){
        //레파지토리에 작성해둔 findByPages 메서드에서 question엔티티의 id를 기준으로 쿼리문을 실행
        return questionPageRepository.findByPages(question.getId());
    }

}
