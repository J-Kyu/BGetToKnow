package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.domain.PublicAnswerGroupDomain;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
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

    private final RoomTicketRepository roomTicketRepository;

    private final RoomService roomService;

    private final UserService userService;

    private final PublicAnswerGroupService publicAnswerGroupService;

    @Transactional
    public Long join(RoomTicketDomain ticket){

        roomTicketRepository.save(ticket);
        return ticket.getId();
    }

    public List<RoomTicketDTO> findAllRoomTickets(){

        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<>();
        List<RoomTicketDomain> roomTicketDomainList = roomTicketRepository.findAllRoomTickets();
        for (RoomTicketDomain ticket: roomTicketDomainList) {
            roomTicketDTOList.add(new RoomTicketDTO(ticket));
        }

        return roomTicketDTOList;
    }


    public RoomTicketDomain findRoomTicket(Long id){
        return roomTicketRepository.findRoomTicket(id);
    }

    @Transactional
    public Long CreateRoomTicket(String roomCode, Long userId){

        //1. Create Room Ticket Domain
        RoomTicketDomain roomTicketDomain = new RoomTicketDomain();

        //2. Search Room
        RoomDomain roomDomain = roomService.findRoomByCode(roomCode);

        //3. Register Room to RoomTicket Domain
        roomTicketDomain.setRoom(roomDomain);

        //4. Search User Domain
        UserDomain userDomain = userService.findUserDomain(userId);

        //5. Register User Domain to Room Ticket Domain
        roomTicketDomain.setUser(userDomain);

        //6. Create Public Answer Group
        PublicAnswerGroupDomain answerGroupDomain =  publicAnswerGroupService.CreatePublicAnswerGroup(roomDomain.getQuestionGroup());

        //7. Register Public Answer Group
       roomTicketDomain.setPublicAnswerGroup(answerGroupDomain);

        //8. Register Room Ticket Domain
        roomTicketRepository.save(roomTicketDomain);

        return roomTicketDomain.getId();
    }

    public RoomTicketDomain findRoomTicketByUserId(Long userId, Long roomId){
        return roomTicketRepository.findRoomTicketWithUserIdAndRoomId(userId,roomId);

    }

}
