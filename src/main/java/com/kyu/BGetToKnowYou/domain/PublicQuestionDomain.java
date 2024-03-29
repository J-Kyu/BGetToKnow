package com.kyu.BGetToKnowYou.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PublicQuestionDomain {

    //id
    @Id @GeneratedValue
    @Column(name="publicQuestion_id")
    private Long id;

    //type
    @Enumerated(EnumType.STRING)
    private QuestionCategoryEnum questionCategory;


    //question string
    private String question;

    @ManyToOne
    @JoinColumn(name = "public_question_group_id")
    private PublicQuestionGroupDomain publicQuestionGroup;

}
