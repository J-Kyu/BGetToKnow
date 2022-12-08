package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicQuestionGroupRepository {

    @Autowired
    private final EntityManager em;

    public void save(PublicQuestionGroupDomain group) {em.persist(group);}

    public PublicQuestionGroupDomain findPublicQuestionGroup(Long id) {return em.find(PublicQuestionGroupDomain.class, id);}

    public List<PublicQuestionGroupDomain> findAllPublicQuestionGroup(){
        return em.createQuery("select g from PublicQuestionGroupDomain g", PublicQuestionGroupDomain.class)
                .getResultList();
    }
    public List<PublicQuestionGroupDomain> findPublicQuestionGroupByType(RoomTypeEnum questionGroupType){
        return em.createQuery("select g from PublicQuestionGroupDomain g where g.questionGroupType = :questionGroupType", PublicQuestionGroupDomain.class)
                .setParameter("questionGroupType", questionGroupType)
                .getResultList();
    }
}
