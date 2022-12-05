package com.kyu.BGetToKnowYou.service;


import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import com.kyu.BGetToKnowYou.respository.PublicQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicQuestionService {


    private final PublicQuestionRepository publicQuestionRepository;

    @Transactional
    public Long join(PublicQuestionDomain question){
        publicQuestionRepository.save(question);
        return question.getId();
    }

    public List<PublicQuestionDomain> findAllQuestions(){return publicQuestionRepository.findAll();}

    public PublicQuestionDomain findQuestion(Long id){return publicQuestionRepository.findQuestion(id);}

}
