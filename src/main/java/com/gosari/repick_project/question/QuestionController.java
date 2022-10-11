package com.gosari.repick_project.question;

import com.gosari.repick_project.answer.Answer;
import com.gosari.repick_project.answer.AnswerForm;
import com.gosari.repick_project.answer.AnswerService;
import com.gosari.repick_project.user.SiteUser;
import com.gosari.repick_project.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/question") //URL프리픽스(prefix)
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    private final QuestionPageRepository questionPageRepository;

    /*질문리스트*/
    @RequestMapping("/list")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="kw",defaultValue = "")String kw){
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging); /*페이징*/
        model.addAttribute("kw", kw); /*kw:검색어*/
        return "question_list";
    }

    /*질문상세*/ //댓글페이징추가
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm,
                         @RequestParam(value="page", defaultValue="0") int page) {

        /*댓글페이징*/
        Question question = this.questionService.getQuestion(id);

        Page<Answer> paging = this.answerService.getList(question, page);
        model.addAttribute("paging", paging);

        model.addAttribute("question", question);

        /*이전글다음글번호와 제목을 html에서 불러올수있게 model.addAttribute() 작성*/
        QuestionPage questionPage = questionPageRepository.findByPages(id);
        model.addAttribute("prevID", questionPage.getPREVID());
        model.addAttribute("prevSub", questionPage.getPREV_SUB());
        model.addAttribute("nextID", questionPage.getNEXTID());
        model.addAttribute("nextSub", questionPage.getNEXT_SUB());

        return "question_detail";
    }

    /*질문등록하기*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    /*질문등록하기*/
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 MultipartFile file) throws Exception{

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser, questionForm.getCategory(), file);
        return "redirect:/question/list";
    }

    /*질문 수정하기 GET*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,
                                 @PathVariable("id") Integer id, Principal principal) {

        Question question = this.questionService.getQuestion(id);

        /*로그인한 사용자와 질문의 작성자가 동일하지않은 경우*/
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        /*수정할 질문의 제목과 내용을 보여줌*/
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        /*수정할 카테고리 보여줌*/
        questionForm.setCategory(question.getCategory());

        return "question_form";
    }

    /*질문 수정하기 POST*/
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id,
                                 MultipartFile file)throws Exception{
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), questionForm.getCategory(),file); //카테고리추가
        return String.format("redirect:/question/detail/%s", id);
    }

    /*질문 삭제하기*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionModify(Principal principal,
                                 @PathVariable("id") Integer id) {

        Question question = this.questionService.getQuestion(id);

        /*로그인한 사용자와 질문의 작성자가 동일하지않은 경우*/
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.questionService.delete(question);
        return "redirect:/";
    }
    /*질문추천*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id){
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}
