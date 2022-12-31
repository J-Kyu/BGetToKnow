package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.domain.CategoryResultDomain;
import com.kyu.BGetToKnowYou.domain.QuestionCategoryEnum;
import com.kyu.BGetToKnowYou.domain.QuestionResultDomain;
import com.kyu.BGetToKnowYou.domain.QuestionResultType;
import com.kyu.BGetToKnowYou.respository.QuestionResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class QuestionResultService {

    private final QuestionResultRepository questionResultRepository;

    private final CategoryResultService categoryResultService;

    @Transactional
    public long join(QuestionResultDomain questionResultDomain){
        questionResultRepository.save(questionResultDomain);
        return questionResultDomain.getId();
    }

    @Transactional
    public QuestionResultDomain CreateQuestionResult(QuestionResultType questionResultType){

        // 1. Create Question Result Domain

        QuestionResultDomain questionResultDomain = new QuestionResultDomain();
        questionResultDomain.setQuestionResultType(questionResultType);
        questionResultRepository.save(questionResultDomain);


        // 2. Create Category Result Domain
        for(int i = 1 ; i <= 5; i++){
            CategoryResultDomain categoryResultDomain = new CategoryResultDomain();
            categoryResultDomain.setAverageScore(0f);
            categoryResultDomain.setQuestionCategory(QuestionCategoryEnum.values()[i]);
            categoryResultDomain.setQuestionResultDomain(questionResultDomain); //save Question Result Domain
            categoryResultService.join(categoryResultDomain); //save each category result

            questionResultDomain.getCategoryResultList().add(categoryResultDomain);
        }

        return questionResultDomain;
    }

    public QuestionResultDomain findOne(Long id){return questionResultRepository.findOne(id);}

    public List<QuestionResultDomain> findAll(){return questionResultRepository.findAll();}

}
