package com.kyu.BGetToKnowYou.respository;


import com.kyu.BGetToKnowYou.domain.CategoryResultDomain;
import com.kyu.BGetToKnowYou.domain.PublicAnswerGroupDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryResultRepository {

    @Autowired
    private EntityManager em;

    public void save(CategoryResultDomain categoryResultDomain){
        em.persist(categoryResultDomain);
    }

    public CategoryResultDomain findCategoryResult(Long id){return em.find(CategoryResultDomain.class, id);}

    public List<CategoryResultDomain> findAllCategoryResult(){
        return em.createQuery("select cg from CategoryResultDomain cg", CategoryResultDomain.class)
                .getResultList();
    }


}
