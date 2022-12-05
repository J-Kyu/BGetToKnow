package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class RoomForm {

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    private int maxNum;

}
