package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class UserDTO {

    public UserDTO(UserDomain userDomain){
        this.id = userDomain.getId();
        this.nickname = userDomain.getNickname();
        this.hashCode = userDomain.getHashCode();
        this.oAuthType = userDomain.getOAuthType();
    }

    final private Long id;

    final private String nickname;

    final private String hashCode;

    @Enumerated(EnumType.STRING)
    final private OAuthTypeEnum oAuthType;

}
