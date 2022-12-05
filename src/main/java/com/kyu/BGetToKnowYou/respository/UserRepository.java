package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    @Autowired
    private final EntityManager em;

    public void save(UserDomain userDomain){ em.persist(userDomain);}

    public UserDomain findOne(Long id) {return em.find(UserDomain.class, id);}

    public List<UserDomain> findAll(){
        return em.createQuery("select m from UserDomain m", UserDomain.class)
                .getResultList();
    }

    public List<UserDomain> findByNickname(String nickname){
        return em. createQuery("select m from UserDomain m where m.nickname = :nickname", UserDomain.class )
                .setParameter("nickname", nickname)
                .getResultList();
    }

}
