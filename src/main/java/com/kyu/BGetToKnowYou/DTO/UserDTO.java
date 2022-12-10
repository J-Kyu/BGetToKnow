package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class UserDTO {

    public UserDTO(){}

    public UserDTO(String nickname, OAuthTypeEnum type){
        this.nickname = nickname;
        this.oAuthType = type;
    }
    public UserDTO(UserDomain userDomain){
        this.id = userDomain.getId();
        this.nickname = userDomain.getNickname();
        this.hashCode = userDomain.getHashCode();
        this.oAuthType = userDomain.getOAuthType();
    }

    private Long id;

    private String nickname;

    private String hashCode;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

}
