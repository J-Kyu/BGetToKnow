package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class RoomTicketDomain {

    @Id
    @GeneratedValue
    @Column(name="roomTicket_id")
    private Long id;




}
