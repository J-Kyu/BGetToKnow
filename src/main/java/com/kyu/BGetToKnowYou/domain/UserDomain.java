package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class UserDomain {

    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

    private String nickname;

    private String hashCode;

//    private List<RoomTicketDomain> joinedRoomTickets;
//
//    private List<RoomTicketDomain> doneRoomTickets;




}
