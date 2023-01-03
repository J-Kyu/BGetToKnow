package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class UserDTO {

    public UserDTO(){}

    public UserDTO(String nickname, OAuthTypeEnum type, String uuid, String accessToken){
        this.nickname = nickname;
        this.oAuthType = type;
        this.uuid = uuid;
        this.genDateTime = LocalDateTime.now();
        this.accessToken = accessToken;

    }
    public UserDTO(UserDomain userDomain){
        this.id = userDomain.getId();
        this.nickname = userDomain.getNickname();
        this.uuid = userDomain.getUuid();
        this.oAuthType = userDomain.getOAuthType();
        this.genDateTime = userDomain.getGenDateTime();
        this.accessToken = userDomain.getAccessToken();
    }

    @JsonIgnore
    private Long id;

    private String nickname;

    private String uuid;

    private LocalDateTime genDateTime;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

    private String accessToken;

}
