package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.RoomDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    @Autowired
    private final EntityManager em;

    public void save(RoomDomain room){ em.persist(room);}

    public RoomDomain findOne(Long id) {return em.find(RoomDomain.class, id);}

    public RoomDomain findRoomByCode(String roomCode) {

        return em. createQuery("select r from RoomDomain r where r.code = :roomCode", RoomDomain.class )
                .setParameter("roomCode", roomCode)
                .getSingleResult();
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