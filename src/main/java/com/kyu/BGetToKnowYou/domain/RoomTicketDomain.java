package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "RoomTickets")
public class RoomTicketDomain {

    @Id
    @GeneratedValue
    @Column(name="roomTicket_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") //column table name
    private UserDomain user;




}
