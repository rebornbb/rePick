package com.gosari.repick_project.question;


import com.gosari.repick_project.answer.Answer;
import com.gosari.repick_project.user.SiteUser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String subject;

    private String content;

    @Column(insertable = false, updatable = false, columnDefinition = "date default LOCALTIMESTAMP")
    private Date createDate;

    /*수정일시*/
    @Column(insertable = true, updatable = true, columnDefinition = "date default LOCALTIMESTAMP")
    private Date modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList; //질문엔티티에 답변엔티티참조 (질문1:답변N)

    @ManyToOne //여러개의 질문이 한명의 사용자에게 작성될수있으므로 @ManyToOne
    private SiteUser author; /*author 속성추가*/

    @ManyToMany //하나의 질문에 여러사람이 추천, 한사람이 여러개의 질문을 추천(대등관계)
    Set<SiteUser> voter; /*추천인은 중복되면 안됨-Set*/

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int countview; /*조회수*/

    private String category; /*카테고리값 저장컬럼*/

    private String filepath;/*파일저장경로*/
    private String filename;/*파일이름*/
}

