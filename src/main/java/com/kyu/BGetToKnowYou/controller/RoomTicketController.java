package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.PublicAnswerDTO;
import com.kyu.BGetToKnowYou.DTO.PublicQuestionDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.domain.*;
import com.kyu.BGetToKnowYou.service.RoomService;
import com.kyu.BGetToKnowYou.service.RoomTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomTicketController {

    private final RoomTicketService roomTicketService;

    private final RoomService roomService;

    @PostMapping(value="/roomTicket/new")
    public String create(@Valid RoomTicketForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        Long roomTicketId = roomTicketService.CreateRoomTicket(form.getRoomCode(), form.getUserId());
        return "ticket created..."+roomTicketId;

    }


    @GetMapping(value="/roomTicket/findAll")
    public List<RoomTicketDTO> GetAllRoomTickets(){
        return roomTicketService.findAllRoomTickets();
    }

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicAnswers")
    public List<PublicAnswerDTO> GetPublicAnswers(@PathVariable("userId")  Long userId,@PathVariable("roomCode")  String roomCode){
        // 1. Find Room Ticket with user Id and Room Code
        RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
        RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());


        // 2. Get Public Answer Group from Room Ticket Domain
        PublicAnswerGroupDomain publicAnswerGroupDomain = roomTicketDomain.getPublicAnswerGroup();

        // 3. Get All Answer Domain from Public Answer Group Domain
        List<PublicAnswerDTO> publicAnswerDTOList = new ArrayList<>();
        for (PublicAnswerDomain answer : publicAnswerGroupDomain.getAnswers() ) {
            publicAnswerDTOList.add(new PublicAnswerDTO(answer));
        }

        return publicAnswerDTOList;
    }

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicQuestions")
    public List<PublicQuestionDTO> GetPublicQuestions(@PathVariable("userId")  Long userId, @PathVariable("roomCode")  String roomCode){

        // 1. Find Room Ticket with user Id and Room Code
        RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
        RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());

        // 2. Get Public Question Group from PA-Gruop Domain (which is from room ticket domain)
        PublicQuestionGroupDomain publicQuestionGroupDomain = roomTicketDomain.getPublicAnswerGroup().getPublicQuestionGroup();

        // 3. Get Questions from Question Group
        List<PublicQuestionDTO> publicQuestionDTOList = new ArrayList<>();
        for (PublicQuestionDomain question: publicQuestionGroupDomain.getQuestions() ) {
            publicQuestionDTOList.add(new PublicQuestionDTO(question));
        }

        return publicQuestionDTOList;

    }

}
