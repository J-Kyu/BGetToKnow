package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.PublicAnswerDomain;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.exception.NoRoomFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
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

    public List<PublicAnswerDomain> findAnswerByAnswerGroupId(Long id){

        List<PublicAnswerDomain> publicAnswerDomainList = new ArrayList<>();
        try {
            publicAnswerDomainList = em.createQuery("select a from PublicAnswerDomain a where a.answerGroup.id = :answerGroupId", PublicAnswerDomain.class)
                    .setParameter("answerGroupId", id)
                    .getResultList();

        }
        catch (NoResultException e){
            throw new NoRoomFoundException("There is no such Answer with question Id: "+id);
        }

        return publicAnswerDomainList;

    }

}
