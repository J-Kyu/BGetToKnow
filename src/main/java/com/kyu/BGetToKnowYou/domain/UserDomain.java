package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j

public class UserDomain {

    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

    private String nickname;

    private String hashCode;

    @OneToMany(mappedBy = "user") //variable name
    private List<RoomTicketDomain> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "adminUser")
    private List<RoomDomain> rooms = new ArrayList<>();

//    private List<RoomTicketDomain> joinedRoomTickets;
//
//    private List<RoomTicketDomain> doneRoomTickets;


    public void AddRoomTicket(RoomTicketDomain ticket){
        this.tickets.add(ticket);
    }




}
