package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicAnswerRepository {

    @Autowired
    private final EntityManager em;

    public void save(PublicAnswerDomain answer){ em.persist(answer);}

    public PublicAnswerDomain findAnswer(Long id) {return em.find(PublicAnswerDomain.class, id);}

    public List<PublicAnswerDomain> findAll(){
        return em.createQuery("select a from PublicAnswerDomain a", PublicAnswerDomain.class)
                .getResultList();
    }

}
