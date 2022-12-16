package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import com.kyu.BGetToKnowYou.domain.PublicQuestionTypeEnum;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PublicQuestionDTO {

    public PublicQuestionDTO(){

    }
    public PublicQuestionDTO(PublicQuestionDomain question){
        this.id = question.getId();
        this.questionType = question.getQuestionType();
        this.question = question.getQuestion();
    }

    @JsonIgnore
    private Long id;

    //type
    @Enumerated(EnumType.STRING)
    private PublicQuestionTypeEnum questionType;


    //question string
    private String question;
}
