package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class RoomDTO {

    public RoomDTO(int maxNum){
        this.roomState = RoomStateEnum.PRE_MEETING;
        this.roomType = RoomTypeEnum.TYPE_A;
        this.maxNum = maxNum;
    }

    public RoomDTO(){
        this.roomState = RoomStateEnum.PRE_MEETING;
        this.roomType = RoomTypeEnum.TYPE_A;
        this.maxNum = 10;
    }

    public RoomDTO(RoomDomain room){
        this.id = room.getId();
        this.code = room.getCode();
        this.roomState = room.getRoomState();
        this.roomType = room.getRoomType();
        this.maxNum = room.getMaxNum();
    }

    private Long id;
    private String code;

    @Enumerated(EnumType.STRING)
    private RoomStateEnum roomState;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    private int maxNum;

}
