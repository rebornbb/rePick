package com.gosari.repick_project.question;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class QuestionPage {
    @Id
    private Integer id;
    private String PREVID;
    private String PREV_SUB;
    private String NEXTID;
    private String NEXT_SUB;
}
