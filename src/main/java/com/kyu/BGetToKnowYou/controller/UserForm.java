package com.kyu.BGetToKnowYou.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String nickname;

}
