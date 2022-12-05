package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.PublicQuestionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PublicQuestionForm {

    @NotEmpty(message = "질문을 작성해주세요")
    private String question;

    private PublicQuestionTypeEnum type;

}
