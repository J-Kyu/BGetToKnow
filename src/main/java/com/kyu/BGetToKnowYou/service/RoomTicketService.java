package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.respository.RoomTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomTicketService {

    private final RoomTicketRepository repository;

    @Transactional
    public Long join(RoomTicketDomain ticket){
        repository.save(ticket);
        return ticket.getId();
    }

    public List<RoomTicketDomain> findAllRoomTickets(){
        return repository.findAllRoomTickets();
    }

    public RoomTicketDomain findRoomTicket(Long id){
        return repository.findRoomTicket(id);
    }

}
