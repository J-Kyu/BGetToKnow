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
        this.accessToken = userDTO.getAccessToken();
    }

    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;
    private String uuid;

    private String accessToken;
    private LocalDateTime genDateTime;

    @OneToMany(mappedBy = "user") //variable name
    private List<RoomTicketDomain> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "adminUser")
    private List<RoomDomain> rooms = new ArrayList<>();


}
