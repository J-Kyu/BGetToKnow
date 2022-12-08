package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PublicAnswerDTO {

    public PublicAnswerDTO(){

    }

    public PublicAnswerDTO(PublicAnswerDomain answerDomain){
        this.id = answerDomain.getId();
        this.answer = answerDomain.getAnswer();
    }

    private Long id;
    private String answer;



}
