package com.kyu.BGetToKnowYou.service;


import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import com.kyu.BGetToKnowYou.respository.PublicQuestionGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicQuestionGroupService {

    private final PublicQuestionGroupRepository publicQuestionGroupRepository;

    @Transactional
    public Long join(PublicQuestionGroupDomain publicQuestionGroup){
        publicQuestionGroupRepository.save(publicQuestionGroup);
        return publicQuestionGroup.getId();
    }

    public List<PublicQuestionGroupDomain> findAllPublicQuestionGroups(){
        return publicQuestionGroupRepository.findAllPublicQuestionGroup();
    }

    public PublicQuestionGroupDomain findPublicQuestionGroup(Long id){
        return publicQuestionGroupRepository.findPublicQuestionGroup(id);
    }

    public PublicQuestionGroupDomain findPublicQuestionGroupByTypeRandomly(RoomTypeEnum type){

        /*
            Get Random Public Question Group from the list of given type Public question groups.
         */
        List<PublicQuestionGroupDomain> publicQuestionGroupDomainList = publicQuestionGroupRepository.findPublicQuestionGroupByType(type);
        Random rand = new Random();
        return publicQuestionGroupDomainList.get(rand.nextInt(publicQuestionGroupDomainList.size()));
    }

}
