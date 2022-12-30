package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.domain.RoomTicketStateEnum;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class RoomTicketDTO {

    public RoomTicketDTO(RoomTicketDomain ticket, RoomDTO roomDTO){
        this.id = ticket.getId();
        this.ticketState = ticket.getTicketState();
        this.roomDTO = roomDTO;
    }

    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomTicketStateEnum ticketState;

    private RoomDTO roomDTO;

}
