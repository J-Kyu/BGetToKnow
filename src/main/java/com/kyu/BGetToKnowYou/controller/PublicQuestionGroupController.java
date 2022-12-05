package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import com.kyu.BGetToKnowYou.service.PublicQuestionGroupService;
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
public class PublicQuestionGroupController {

    private final PublicQuestionGroupService service;

    @PostMapping(value = "/PublicQuestionGroup/new")
    public String create(@Valid PublicQuestionGroupForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        PublicQuestionGroupDomain group = new PublicQuestionGroupDomain();
        service.join(group);

        return "Public Question Group Succeed";
    }


    @GetMapping(value = "/publicQuestionGroup/findAll")
    public List<PublicQuestionGroupDomain> findAllQuestionGroups(){
        return service.findAllPublicQuestionGroups();
    }

}
