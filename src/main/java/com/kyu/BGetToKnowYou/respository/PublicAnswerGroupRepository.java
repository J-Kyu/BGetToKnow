package com.kyu.BGetToKnowYou.respository;


import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import com.kyu.BGetToKnowYou.domain.PublicAnswerGroupDomain;
import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicAnswerGroupRepository {

    @Autowired
    private final EntityManager em;

    public void save(PublicAnswerGroupDomain group){em.persist(group);}

    public PublicAnswerGroupDomain findPublicAnswerGroup(Long id){return em.find(PublicAnswerGroupDomain.class, id);}

    public List<PublicAnswerGroupDomain> findAllPublicAnswerGroup(){
        return em.createQuery("select g from PublicAnswerGroupDomain g", PublicAnswerGroupDomain.class)
                .getResultList();
    }

}
