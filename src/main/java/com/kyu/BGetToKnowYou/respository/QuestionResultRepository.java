package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.QuestionResultDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionResultRepository {

    @Autowired
    private final EntityManager em;

    public void save(QuestionResultDomain questionResultDomain){em.persist(questionResultDomain);}
    public QuestionResultDomain findOne(Long id) {return em.find(QuestionResultDomain.class, id);}
    public List<QuestionResultDomain> findAll(){
        return em.createQuery("select m from QuestionResultDomain m", QuestionResultDomain.class)
                .getResultList();
    }
}
