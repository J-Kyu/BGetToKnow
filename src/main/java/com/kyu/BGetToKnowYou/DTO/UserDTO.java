package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class UserDTO {

    public UserDTO(){}

    public UserDTO(String nickname, OAuthTypeEnum type, String uuid){
        this.nickname = nickname;
        this.oAuthType = type;
        this.uuid = uuid;
    }
    public UserDTO(UserDomain userDomain){
        this.id = userDomain.getId();
        this.nickname = userDomain.getNickname();
        this.uuid = userDomain.getUuid();
        this.oAuthType = userDomain.getOAuthType();
    }

    @JsonIgnore
    private Long id;

    private String nickname;

    private String uuid;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

}
