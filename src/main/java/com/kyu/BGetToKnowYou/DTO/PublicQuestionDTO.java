package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import com.kyu.BGetToKnowYou.domain.PublicQuestionTypeEnum;
import com.kyu.BGetToKnowYou.domain.QuestionCategoryEnum;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PublicQuestionDTO {

    public PublicQuestionDTO(){

    }
    public PublicQuestionDTO(PublicQuestionDomain question){
        this.id = question.getId();
//        this.questionType = question.getQuestionType();
        this.question = question.getQuestion();
        this.questionCategory = question.getQuestionCategory();
    }

    private Long id;

    //type
    @Enumerated(EnumType.STRING)
    private PublicQuestionTypeEnum questionType;

    @Enumerated(EnumType.STRING)
    private QuestionCategoryEnum questionCategory;


    //question string
    private String question;
}
