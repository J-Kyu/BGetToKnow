package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class PublicAnswerGroupDomain {

    @Id
    @GeneratedValue
    @Column(name="publicAnswerGroup_id")
    private Long id;


    @OneToMany(mappedBy = "answerGroup")
    private List<PublicAnswerDomain> answers = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "public_question_group_id")
    private PublicQuestionGroupDomain publicQuestionGroup;


}
