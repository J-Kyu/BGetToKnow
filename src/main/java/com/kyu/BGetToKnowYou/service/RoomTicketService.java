package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.respository.RoomTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomTicketService {

    private final RoomTicketRepository repository;

    @Transactional
    public Long join(RoomTicketDomain ticket){

        repository.save(ticket);
        return ticket.getId();
    }

    public List<RoomTicketDTO> findAllRoomTickets(){

        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<>();
        List<RoomTicketDomain> roomTicketDomainList = repository.findAllRoomTickets();
        for (RoomTicketDomain ticket: roomTicketDomainList) {
            roomTicketDTOList.add(new RoomTicketDTO(ticket));
        }

        return roomTicketDTOList;
    }


    public RoomTicketDomain findRoomTicket(Long id){
        return repository.findRoomTicket(id);
    }

}
