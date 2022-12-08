package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import com.kyu.BGetToKnowYou.domain.PublicAnswerGroupDomain;
import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import com.kyu.BGetToKnowYou.respository.PublicAnswerGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicAnswerGroupService {

    private final PublicAnswerGroupRepository publicAnswerGroupRepository;

    private final PublicAnswerService publicAnswerService;

    @Transactional
    public Long join(PublicAnswerGroupDomain group){
        publicAnswerGroupRepository.save(group);
        return group.getId();
    }

    public List<PublicAnswerGroupDomain> findAllPublicAnswerGroups(){return publicAnswerGroupRepository.findAllPublicAnswerGroup();}

    public PublicAnswerGroupDomain findPublicAnswerGroup(Long id) {return publicAnswerGroupRepository.findPublicAnswerGroup(id);}


    public PublicAnswerGroupDomain CreatePublicAnswerGroup(PublicQuestionGroupDomain questionGroupDomain){

        //1. Create PA-Group Domain(Public Answer Group Domain)
        PublicAnswerGroupDomain answerGroupDomain = new PublicAnswerGroupDomain();

        //2. Register PQ-GroupDomain (Public Question Group Domain) to PA-Group Domain
        answerGroupDomain.setPublicQuestionGroup(questionGroupDomain);

        //3. Create Public Answer Domain same number of Public Question Domain registered at PQ-Domain Group
        List<PublicAnswerDomain> answerDomainList = new ArrayList<>();

        for (PublicQuestionDomain question: questionGroupDomain.getQuestions() ) {
            PublicAnswerDomain answerDomain = new PublicAnswerDomain();
            //set answer domain
            answerDomain.setQuestion(question);
            answerDomain.setAnswerGroup(answerGroupDomain);

            //register Answer Domain
            publicAnswerService.join(answerDomain);

            answerDomainList.add(answerDomain);
        }

        //4. Register Answer Domain to PA-Domain Group
        answerGroupDomain.setAnswers(answerDomainList);

        //5.  Register PA-Group Domain
        publicAnswerGroupRepository.save(answerGroupDomain);



        return answerGroupDomain;
    }

}
