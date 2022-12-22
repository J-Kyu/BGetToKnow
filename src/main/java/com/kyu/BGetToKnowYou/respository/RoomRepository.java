package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.exception.NoRoomFoundException;
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
public class RoomRepository {

    @Autowired
    private final EntityManager em;

    public void save(RoomDomain room){ em.persist(room);}

    public RoomDomain findOne(Long id) {return em.find(RoomDomain.class, id);}

    public RoomDomain findRoomByCode(String roomCode) {

        RoomDomain roomDomain = new RoomDomain();

        try {
            roomDomain = em.createQuery("select r from RoomDomain r where r.code = :roomCode", RoomDomain.class)
                    .setParameter("roomCode", roomCode)
                    .getSingleResult();
        }
        catch (NoResultException e){
            throw new NoRoomFoundException("There is no such room code: "+roomCode);
        }

        return roomDomain;

    }


    public List<RoomDomain> findAll(){
        return em.createQuery("select m from RoomDomain m", RoomDomain.class)
                .getResultList();
    }

    public List<RoomDomain> findByCode(String code){
        return em. createQuery("select m from RoomDomain m where m.code = :code", RoomDomain.class )
                .setParameter("code", code)
                .getResultList();
    }

}
