package com.kyu.BGetToKnowYou.service;


import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.domain.CategoryResultDomain;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.respository.CategoryResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryResultService {

    private final CategoryResultRepository categoryResultRepository;


    @Transactional
    public long join(CategoryResultDomain categoryResultDomain){


        categoryResultRepository.save(categoryResultDomain);
        return categoryResultDomain.getId();
    }

    public CategoryResultDomain findOne(long id){return categoryResultRepository.findCategoryResult(id);}

    public List<CategoryResultDomain> findAll(){return categoryResultRepository.findAllCategoryResult();}

    @Transactional
    public CategoryResultDomain updateCategoryAverageScore(long id,float score){
        CategoryResultDomain categoryResultDomain = this.findOne(id);
        categoryResultDomain.setAverageScore(score);
        return categoryResultDomain;
    }


    public void updateCategoryResult(){

    }

}
