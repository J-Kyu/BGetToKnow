package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

    @Autowired
    private final EntityManager em;

    public void save(UserDomain userDomain){ em.persist(userDomain);}

    public UserDomain findOne(Long id) {
        UserDomain userDomain = new UserDomain();

        userDomain = em.find(UserDomain.class, id);

        //prevent null
        if (userDomain ==  null){
            throw new NoneExistingRowException("No such user id: "+id);
        }

        return userDomain;
    }

    public List<UserDomain> findAll(){
        return em.createQuery("select m from UserDomain m", UserDomain.class)
                .getResultList();
    }

    public List<UserDomain> findByNickname(String nickname){
        return em. createQuery("select m from UserDomain m where m.nickname = :nickname", UserDomain.class )
                .setParameter("nickname", nickname)
                .getResultList();
    }

    public UserDomain findByUUID(String uuid){
        UserDomain userDomain = new UserDomain();

        try {
            userDomain = em.createQuery("select m from UserDomain m where m.uuid = :uuid", UserDomain.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        }
        catch (NoResultException e){
            throw new NoneExistingRowException("There is no user with given UUID: "+ uuid);
        }
        return userDomain;

    }

}
