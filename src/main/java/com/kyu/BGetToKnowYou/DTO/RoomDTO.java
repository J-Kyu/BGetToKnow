package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class RoomDTO {
    private String code;

    @Enumerated(EnumType.STRING)
    private RoomStateEnum roomState;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    private int maxNum;

}
