package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.service.RoomTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomTicketController {

    private final RoomTicketService service;

    @PostMapping(value="/roomTicket/new")
    public String create(@Valid RoomTicketForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        RoomTicketDomain ticket = new RoomTicketDomain();
        service.join(ticket);
        return "ticket created...";

    }


    @GetMapping(value="/roomTicket/findAll")
    public List<RoomTicketDTO> GetAllRoomTickets(){
        return service.findAllRoomTickets();
    }
}
