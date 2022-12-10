package com.kyu.BGetToKnowYou.respository;

import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomTicketRepository {

    @Autowired
    private final EntityManager em;

    public void save(RoomTicketDomain ticket){em.persist(ticket);}

    public RoomTicketDomain findRoomTicket(Long id){ return em.find(RoomTicketDomain.class, id);}

    public List<RoomTicketDomain> findAllRoomTickets(){
        return em.createQuery("select rt from RoomTicketDomain rt", RoomTicketDomain.class)
                .getResultList();
    }

    public RoomTicketDomain findRoomTicketWithUserIdAndRoomId(Long userId, Long roomId){

        RoomTicketDomain roomTicketDomain = new RoomTicketDomain();

        try{
            roomTicketDomain = em.createQuery("select rt from RoomTicketDomain rt where rt.user.id = :userId and rt.room.id = :roomId", RoomTicketDomain.class)
                    .setParameter("userId", userId)
                    .setParameter("roomId", roomId)
                    .getSingleResult();
        }
        catch (NoResultException e){
            throw new NoneExistingRowException("No Room Ticket with userId: "+userId +" roomId: "+roomId);
        }

        return roomTicketDomain;
    }

}
