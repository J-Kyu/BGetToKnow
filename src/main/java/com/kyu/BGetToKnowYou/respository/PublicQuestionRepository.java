package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicQuestionRepository {

    @Autowired
    private final EntityManager em;

    public void save(PublicQuestionDomain question){em.persist(question);}

    public PublicQuestionDomain findQuestion(Long id) {return em.find(PublicQuestionDomain.class, id);}

    public List<PublicQuestionDomain> findAll(){
        return em.createQuery("select q from PublicQuestionDomain q", PublicQuestionDomain.class)
                .getResultList();
    }

}
