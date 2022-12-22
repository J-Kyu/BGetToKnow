package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PublicAnswerDomain {

    @Id @GeneratedValue
    @Column(name="publicAnswer_id")
    private Long id;

    private String answer;

    private int score = 0;

    @OneToOne
    @JoinColumn(name = "public_question_id")
    private PublicQuestionDomain question;

    @ManyToOne
    @JoinColumn(name = "answer_group_id")
    private PublicAnswerGroupDomain answerGroup;

}
