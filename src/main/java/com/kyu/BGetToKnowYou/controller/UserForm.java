package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String nickname;

    private String uuid;

    private String accessToken;

    @Enumerated(EnumType.STRING)
    private OAuthTypeEnum oAuthType;

}
