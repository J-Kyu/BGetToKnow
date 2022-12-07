package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import lombok.Getter;

@Getter
public class RoomTicketDTO {

    public RoomTicketDTO(RoomTicketDomain ticket){
        this.id = ticket.getId();
    }

    private Long id;

}
