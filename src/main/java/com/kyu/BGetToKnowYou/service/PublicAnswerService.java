package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.respository.PublicAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicAnswerService {

    private final PublicAnswerRepository publicAnswerRepository;

    @Transactional
    public Long join(PublicAnswerDomain answer){
        publicAnswerRepository.save(answer);
        return answer.getId();
    }

    public List<PublicAnswerDomain> findAllAnswers(){return publicAnswerRepository.findAll();}

    public PublicAnswerDomain findAnswer(Long id) {return publicAnswerRepository.findAnswer(id);}

    public List<PublicAnswerDomain> findAnswerByAnswerGroupId(Long id){
        return publicAnswerRepository.findAnswerByAnswerGroupId(id);
    }

}
