package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.service.PublicAnswerService;
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
public class PublicAnswerController {

    private final PublicAnswerService publicAnswerService;

    @PostMapping("/publicAnswer/new")
    public String create(@Valid PublicAnswerForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        PublicAnswerDomain publicAnswer = new PublicAnswerDomain();
        publicAnswer.setAnswer(form.getAnswer());
        publicAnswerService.join(publicAnswer);

        return "redirect:/";
    }

    @GetMapping("/publicAnswer/findAll")
    public List<PublicAnswerDomain> GetUserList(){
        return publicAnswerService.findAllAnswers();
    }


}
