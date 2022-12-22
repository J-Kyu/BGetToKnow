package com.kyu.BGetToKnowYou.domain;

import com.kyu.BGetToKnowYou.DTO.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j

public class UserDomain {

    public UserDomain(){}

    public UserDomain(UserDTO userDTO){
        this.oAuthType = userDTO.getOAuthType();
        this.uuid = userDTO.getUuid();
        this.nickname = userDTO.getNickname();
        this.genDateTime = LocalDateTime.now();
    }

    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

    private String nickname;

    private String uuid;

    private LocalDateTime genDateTime;

    @OneToMany(mappedBy = "user") //variable name
    private List<RoomTicketDomain> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "adminUser")
    private List<RoomDomain> rooms = new ArrayList<>();

    public void AddRoomTicket(RoomTicketDomain ticket){
        this.tickets.add(ticket);
    }

}
