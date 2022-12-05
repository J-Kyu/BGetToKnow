package com.kyu.BGetToKnowYou.service;


import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import com.kyu.BGetToKnowYou.respository.PublicQuestionGroupRepository;
import com.kyu.BGetToKnowYou.respository.PublicQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicQuestionGroupService {

    private final PublicQuestionGroupRepository repository;

    @Transactional
    public Long join(PublicQuestionGroupDomain publicQuestionGroup){
        repository.save(publicQuestionGroup);
        return publicQuestionGroup.getId();
    }

    public List<PublicQuestionGroupDomain> findAllPublicQuestionGroups(){
        return repository.findAllPublicQuestionGroup();
    }

    public PublicQuestionGroupDomain findPublicQuestionGroup(Long id){
        return repository.findPublicQuestionGroup(id);
    }

}
