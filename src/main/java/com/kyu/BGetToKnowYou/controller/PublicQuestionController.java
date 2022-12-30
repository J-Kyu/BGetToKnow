package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.service.PublicQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicQuestionController {

    private final PublicQuestionService publicQuestionService;

    @PostMapping("/publicQuestion/new")
    public String create(@Valid PublicQuestionForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        PublicQuestionDomain question = new PublicQuestionDomain();
        question.setQuestion(form.getQuestion());
//        question.setQuestionType(form.getType());
        publicQuestionService.join(question);

        return "redirect:/";
    }

    @GetMapping("/publicQuestion/findAll")
    public List<PublicQuestionDomain> GetUserList(){
        return publicQuestionService.findAllQuestions();
    }

}
