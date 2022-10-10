package com.gosari.repick_project.question;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class QuestionForm {

    @NotEmpty(message = "제목은 필수항목입니다.") /*Null허용치않음*/
    @Size(max=200) /*최대길이200byte*/
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    /*카테고리메세지*/
    @NotBlank(message = "카테고리선택은 필수항목입니다.")
    private String category;

}
