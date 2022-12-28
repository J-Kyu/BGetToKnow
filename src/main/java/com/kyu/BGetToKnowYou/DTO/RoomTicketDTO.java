package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.domain.RoomTicketStateEnum;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class RoomTicketDTO {

    public RoomTicketDTO(RoomTicketDomain ticket){
        this.id = ticket.getId();
        this.ticketState = ticket.getTicketState();
    }

    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomTicketStateEnum ticketState;

}
